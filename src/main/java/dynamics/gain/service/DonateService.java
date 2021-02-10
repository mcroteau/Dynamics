package dynamics.gain.service;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.model.*;
import dynamics.gain.common.Constants;
import dynamics.gain.common.Dynamics;
import dynamics.gain.common.Utils;
import dynamics.gain.model.*;
import dynamics.gain.repository.PlanRepo;
import dynamics.gain.repository.UserRepo;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import xyz.strongperched.Parakeet;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@PropertySource("classpath:application.properties")
public class DonateService {

    private static final Logger log = Logger.getLogger(DonateService.class);

    private static final String MONTHLY = "monthly donation";

    @Value("${stripe.api.key}")
    private String apiKey;

    @Value("${stripe.dev.api.key}")
    private String devApiKey;

    Gson gson = new Gson();

    @Autowired
    UserRepo userRepo;

    @Autowired
    PlanRepo planRepo;

    @Autowired
    AuthService authService;

    @Autowired
    EmailService emailService;

    @Autowired
    Environment env;

    public String getUserPermission(String id){
        return Constants.ACCOUNT_MAINTENANCE + id;
    }


    public Donation make(DonationInput donationInput){

        Donation donation = new Donation();
        donation.setProcessed(false);
        if(!Utils.isValidMailbox(donationInput.getEmail())){
            donation.setStatus("Email is invalid, please try again!");
            return donation;
        }
        if(donationInput.getCreditCard().equals("")){
            donation.setStatus("Credit card is empty, please try again");
            return donation;
        }
        if(donationInput.getExpMonth() == null){
            donation.setStatus("Expiration month is empty, please try again");
            return donation;
        }
        if(donationInput.getExpYear() == null){
            donation.setStatus("Expiration year is empty, please try again");
            return donation;
        }
        if(donationInput.getCvc().equals("")){
            donation.setStatus("Cvc is empty! please try again.");
            return donation;
        }
        donation.setStatus("hasn't processed yet...");

        try{

            Stripe.apiKey = getApiKey();
            User user = userRepo.getByUsername(donationInput.getEmail());
            String password = Utils.getRandomString(7);

            if(user == null){
                log.info("set password clean");
                user = new User();
                user.setUsername(donationInput.getEmail());
                user.setPassword(Parakeet.dirty(password));
                user = userRepo.save(user);
            }

            Map<String, Object> card = new HashMap<>();
            card.put("number", donationInput.getCreditCard());
            card.put("exp_month", donationInput.getExpMonth());
            card.put("exp_year", donationInput.getExpYear());
            card.put("cvc", donationInput.getCvc());
            Map<String, Object> params = new HashMap<>();
            params.put("card", card);

            Token token = Token.create(params);

            if(user.getStripeUserId() != null &&
                    !user.getStripeUserId().equals("")){
                Customer oldCustomer = Customer.retrieve(user.getStripeUserId());
                try {
                    oldCustomer.delete();
                }catch(Exception e){ log.info("stale stripe customer id");}
            }

            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("email", donationInput.getEmail());
            customerParams.put("source", token.getId());
            Customer customer = com.stripe.model.Customer.create(customerParams);

            log.info(customer.getDefaultSource());

            user.setStripeUserId(customer.getId());
            userRepo.update(user);


            Long amountInCents = donationInput.getAmount().multiply(new BigDecimal(100)).longValue();
            log.info("amount ~ " + amountInCents);

            String recurringMessage = donationInput.isRecurring() ? MONTHLY : "";
            log.info("recurring " + donationInput.isRecurring());

            if(donationInput.isRecurring()) {

                DynamicsPlan storedPlan = planRepo.getPlanAmount(donationInput.getAmount());

                if (storedPlan != null) {
                    DynamicsProduct savedProduct = planRepo.getProduct(storedPlan.getProductId());
                    Product stripeProduct = Product.retrieve(savedProduct.getStripeId());
                    createSubscription(amountInCents, donationInput, savedProduct, storedPlan, stripeProduct, customer, user, password);
                }

                if(storedPlan == null){

                    DynamicsPlan dynamicsPlan = new DynamicsPlan();
                    dynamicsPlan.setAmount(donationInput.getAmount());
                    dynamicsPlan.setNickname(donationInput.getAmount() + " " + recurringMessage);

                    Map<String, Object> productParams = new HashMap<>();
                    productParams.put("name", dynamicsPlan.getNickname());
                    com.stripe.model.Product stripeProduct = com.stripe.model.Product.create(productParams);

                    DynamicsProduct dynamicsProduct = new DynamicsProduct();
                    dynamicsProduct.setNickname(dynamicsPlan.getNickname());
                    dynamicsProduct.setStripeId(stripeProduct.getId());
                    DynamicsProduct savedProduct = planRepo.saveProduct(dynamicsProduct);

                    Plan stripePlan = genStripePlan(amountInCents, dynamicsPlan, stripeProduct);

                    dynamicsPlan.setStripeId(stripePlan.getId());
                    dynamicsPlan.setProductId(savedProduct.getId());
                    DynamicsPlan savedPlan = planRepo.savePlan(dynamicsPlan);

                    createSubscription(amountInCents, donationInput, savedProduct, savedPlan, stripeProduct, customer, user, password);

                }
            }

            if(!donationInput.isRecurring()){
//                log.info("token : " + token.toString());
//                log.info("customer : " + customer.toString());
                Map<String, Object> chargeParams = new HashMap<>();
                chargeParams.put("amount", amountInCents);
                chargeParams.put("customer", customer.getId());
                chargeParams.put("card", token.getCard().getId());
                chargeParams.put("currency", "usd");

                Charge charge = Charge.create(chargeParams);
                donation.setChargeId(charge.getId());

                user.setStripeUserId(customer.getId());
                user.setStripeChargeId(charge.getId());
                userRepo.update(user);
            }

            donation.setAmount(donationInput.getAmount());
            donation.setUser(user);
            donation.setProcessed(true);
            donation.setStatus("Successfully processed donation");

        }catch(Exception ex){
            donation.setStatus("Please contact support@dynamicsgain.org, the payment didn't process");
            ex.printStackTrace();
        }

        return donation;
    }

    private Plan genStripePlan(Long amountInCents, DynamicsPlan dynamicsPlan, Product stripeProduct) throws Exception {
        Map<String, Object> planParams = new HashMap<>();
        planParams.put("product", stripeProduct.getId());
        planParams.put("nickname", dynamicsPlan.getNickname());
        planParams.put("interval", dynamicsPlan.getFrequency());
        planParams.put("currency", dynamicsPlan.getCurrency());
        planParams.put("amount", amountInCents);
        Plan stripePlan = Plan.create(planParams);
        return stripePlan;
    }

    private boolean createSubscription(Long amountInCents, DonationInput donationInput, DynamicsProduct savedProduct, DynamicsPlan dynamicsPlan, Product stripeProduct, Customer customer, User user, String password) throws Exception{
        try {
            Subscription subscription = com.stripe.model.Subscription.retrieve(user.getStripeSubscriptionId());
            subscription.cancel();
        }catch(Exception e){
            log.info("cannot cancel previous donation");
        }

        Map<String, Object> itemParams = new HashMap<>();
        itemParams.put("plan", dynamicsPlan.getStripeId());

        Map<String, Object> itemsParams = new HashMap<>();
        itemsParams.put("0", itemParams);

        Map<String, Object> subscriptionParams = new HashMap<>();
        subscriptionParams.put("customer", customer.getId());
        subscriptionParams.put("items", itemsParams);

        Subscription s = com.stripe.model.Subscription.create(subscriptionParams);

        user.setPlanId(dynamicsPlan.getId());
        user.setStripeSubscriptionId(s.getId());

        userRepo.update(user);

        return true;
    }

    public String select(ModelMap modelMap, RedirectAttributes redirect){
        if(!authService.isAuthenticated()){
            redirect.addFlashAttribute("message", "Please signin to continue");
            return "redirect:/";
        }
        List<DynamicsPlan> dynamicsPlans = planRepo.getList();
        modelMap.put("dynamicsPlans", dynamicsPlans);
        return "plan/select";
    }

    public String upgrade(ModelMap modelMap, RedirectAttributes redirect){
        if(!authService.isAuthenticated()){
            redirect.addFlashAttribute("message", "Please signin to continue");
            return "redirect:/";
        }

        User user = authService.getUser();
        List<DynamicsPlan> dynamicsPlans = planRepo.getList();

        modelMap.put("user", user);
        modelMap.put("dynamicsPlans", dynamicsPlans);

        return "plan/upgrade";
    }

    public String confirm(Long id, ModelMap modelMap){
        if(!authService.isAuthenticated()){
            return "redirect:/unauthorized";
        }

        User user = authService.getUser();
        String permission = getUserPermission(Long.toString(user.getId()));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/unauthorized";
        }

        DynamicsPlan dynamicsPlan = planRepo.getPlan(id);
        modelMap.put("dynamicsPlan", dynamicsPlan);

        return "dynamicsPlan/confirm";
    }


    public String start(Long id, User user, RedirectAttributes redirect){
        if(!authService.isAuthenticated()){
            return "redirect:/unauthorized";
        }

        String permission = getUserPermission(Long.toString(user.getId()));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/unauthorized";
        }

        Stripe.apiKey = getApiKey();
        DynamicsPlan dynamicsPlan = planRepo.getPlan(id);

        try {
            Subscription subscription = com.stripe.model.Subscription.retrieve(user.getStripeSubscriptionId());
            subscription.cancel();

            Map<String, Object> customerParams = new HashMap<String, Object>();
            customerParams.put("email", user.getUsername());
//            customerParams.put("source", token);
            Customer customer = com.stripe.model.Customer.create(customerParams);

            Map<String, Object> itemParams = new HashMap<>();
            itemParams.put("plan", dynamicsPlan.getStripeId());

            Map<String, Object> itemsParams = new HashMap<>();
            itemsParams.put("0", itemParams);

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("customer", customer.getId());
            params.put("items", itemsParams);

            Subscription s = com.stripe.model.Subscription.create(params);

            user.setStripeUserId(customer.getId());
            user.setPlanId(id);
            user.setStripeSubscriptionId(s.getId());
            userRepo.updatePlan(user);

            redirect.addFlashAttribute("message", "Congratulations, you are now ready! " + dynamicsPlan.getNickname() + " for " + dynamicsPlan.getProjectLimit() + " websites!");

        }catch(Exception ex){
            redirect.addFlashAttribute("message", "Something went wrong, nothing should have been charged. Please try again, or contact us mail@okay.page");
            String message = user.getUsername() + " " + dynamicsPlan.getNickname();
            emailService.send("croteau.mike@gmail.com", "subscription issue", message);
            ex.printStackTrace();
        }
        return "redirect:/dynamicsPlan/select";
    }


    public String cancel(User user){
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        String permission = getUserPermission(Long.toString(user.getId()));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/unauthorized";
        }


        try{
            Stripe.apiKey = getApiKey();
            Subscription subscription = com.stripe.model.Subscription.retrieve(user.getStripeSubscriptionId());
            subscription.cancel();
        }catch(Exception e){
            e.printStackTrace();
        }

        user.setStripeSubscriptionId(null);
        user.setPlanId(null);
        userRepo.updatePlan(user);

        return "redirect:/user/edit/" + user.getId();
    }


    public String list(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/unauthorized";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }
        List<DynamicsPlan> dynamicsPlans = planRepo.getList();
        modelMap.put("dynamicsPlans", dynamicsPlans);
        return "plan/index";
    }

    public String create(){
        if(!authService.isAuthenticated()){
            return "redirect:/unauthorized";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }
        return "plan/create";
    }


    public String save(DynamicsPlan dynamicsPlan, RedirectAttributes redirectAttributes){
        if(!authService.isAuthenticated()){
            return "redirect:/unauthorized";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }
        if(dynamicsPlan.getAmount().multiply(new BigDecimal(100)).longValue() >= 1000){
            redirectAttributes.addFlashAttribute("message", "You just entered an amount larger than $1000.00");
            return "redirect:/donate/list";
        }
        if(dynamicsPlan.getNickname().equals("")){
            redirectAttributes.addFlashAttribute("message", "blank nickname");
            return "redirect:/donate/list";
        }

        try {

            Stripe.apiKey = getApiKey();

            Map<String, Object> productParams = new HashMap<>();
            productParams.put("name", dynamicsPlan.getNickname());
            com.stripe.model.Product stripeProduct = com.stripe.model.Product.create(productParams);

            DynamicsProduct dynamicsProduct = new DynamicsProduct();
            dynamicsProduct.setNickname(dynamicsPlan.getNickname());
            dynamicsProduct.setStripeId(stripeProduct.getId());
            DynamicsProduct savedDynamicsProduct = planRepo.saveProduct(dynamicsProduct);


            Map<String, Object> planParams = new HashMap<>();
            planParams.put("product", stripeProduct.getId());
            planParams.put("nickname", dynamicsPlan.getNickname());
            planParams.put("interval", dynamicsPlan.getFrequency());
            planParams.put("currency", dynamicsPlan.getCurrency());
            planParams.put("amount", dynamicsPlan.getAmount());
            com.stripe.model.Plan stripePlan = com.stripe.model.Plan.create(planParams);

            dynamicsPlan.setStripeId(stripePlan.getId());
            dynamicsPlan.setProductId(savedDynamicsProduct.getId());
            planRepo.savePlan(dynamicsPlan);

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return "redirect:/dynamicsPlan/list";
    }

    public String delete(Long id, RedirectAttributes redirect){
        if(!authService.isAuthenticated()){
            return "redirect:/unauthorized";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }

        DynamicsPlan dynamicsPlan = planRepo.getPlan(id);
        DynamicsProduct dynamicsProduct = planRepo.getProduct(dynamicsPlan.getProductId());

        try{
            com.stripe.model.Plan plan = com.stripe.model.Plan.retrieve(dynamicsPlan.getStripeId());
            plan.delete();
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            com.stripe.model.Product product = com.stripe.model.Product.retrieve(dynamicsProduct.getStripeId());
            product.delete();
        }catch(Exception e){
            e.printStackTrace();
        }

        List<User> users = userRepo.getPlanList(dynamicsPlan.getId());
        for(User user : users){
            userRepo.removePlan(user.getId());
        }

        planRepo.deletePlan(dynamicsPlan.getId());
        planRepo.deleteProduct(dynamicsProduct.getId());

        return "redirect:/plan/list";
    }

    public String cleanup(){
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator()){
            return "redirect:/";
        }
        try {
            Stripe.apiKey = getApiKey();

            Map<String, Object> params = new HashMap<>();
            PlanCollection planCollection = com.stripe.model.Plan.list(params);
            List<com.stripe.model.Plan> plans = planCollection.getData();
            for(com.stripe.model.Plan plan: plans){
                plan.delete();
            }

            ProductCollection productCollection = com.stripe.model.Product.list(params);
            List<com.stripe.model.Product> products = productCollection.getData();
            for(com.stripe.model.Product product: products){
                product.delete();
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return "redirect:/";
    }

    private String getApiKey(){
        if(Dynamics.isDevEnv(env)){
            return devApiKey;
        }
        return apiKey;
    }

}
