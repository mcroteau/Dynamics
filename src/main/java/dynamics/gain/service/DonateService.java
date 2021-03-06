package dynamics.gain.service;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import dynamics.gain.common.Dynamics;
import dynamics.gain.common.Constants;
import dynamics.gain.model.*;
import dynamics.gain.repository.DonationRepo;
import dynamics.gain.repository.LocationRepo;
import dynamics.gain.repository.StripeRepo;
import dynamics.gain.repository.UserRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import xyz.strongperched.Parakeet;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@PropertySource("classpath:application.properties")
public class DonateService {

    private static final Logger log = Logger.getLogger(DonateService.class);

    Gson gson = new Gson();

    @Autowired
    UserRepo userRepo;

    @Autowired
    LocationRepo locationRepo;

    @Autowired
    StripeRepo stripeRepo;

    @Autowired
    DonationRepo donationRepo;

    @Autowired
    AuthService authService;

    @Autowired
    PhoneService phoneService;

    @Autowired
    PersistenceService persistenceService;

    public String getUserPermission(String id){
        return Constants.ACCOUNT_MAINTENANCE + id;
    }

    public String index(ModelMap modelMap) {
        modelMap.put("inDonateMode", true);
        return "donate/index";
    }

    public String location(Long id, ModelMap modelMap) {
        Location location = locationRepo.get(id);
        String devKey = persistenceService.get("dev." + id);
        String liveKey = persistenceService.get("live." + id);

        if(!devKey.equals("")
                |!liveKey.equals("")) {
            modelMap.put("inDonateMode", true);
        }
        modelMap.put("location", location);
        return "donate/index";
    }

    public Donation make(DonationInput donationInput){

        Donation donation = new Donation();
        if(donationInput.getAmount() == null){
            donation.setStatus("$ amount not passed in, please give it another go!");
            return donation;
        }
        if(!Dynamics.isValidMailbox(donationInput.getEmail())){
            donation.setStatus("Email is invalid, please give it another go!");
            return donation;
        }
        if(donationInput.getCreditCard().equals("")){
            donation.setStatus("Credit card is empty, please give it another go!");
            return donation;
        }
        if(donationInput.getExpMonth() == null){
            donation.setStatus("Expiration month is empty, please give it another go!");
            return donation;
        }
        if(donationInput.getExpYear() == null){
            donation.setStatus("Expiration year is empty, please give it another go!");
            return donation;
        }
        if(donationInput.getCvc().equals("")){
            donation.setStatus("Cvc is empty, please give it another go!");
            return donation;
        }

        if(donationInput.getLocation() != null &&
                donationInput.getLocationId() == null){
            donationInput.setLocationId(donationInput.getLocation());
        }

        phoneService.support("Gaining Momentum ~ " + donationInput.getEmail());

        try {

            Stripe.apiKey = persistenceService.getApiKey(donationInput.getLocationId());
            User user = userRepo.getByUsername(donationInput.getEmail());
            String password = Dynamics.getRandomString(7);

            if (user == null) {
                user = new User();
                user.setUsername(donationInput.getEmail());
                user.setPassword(Parakeet.dirty(password));
                user.setDateCreated(Dynamics.getDate());
                user = userRepo.save(user);
                user.setCleanPassword(password);
            }

            donation.setProcessed(false);
            donation.setAmount(donationInput.getAmount());
            donation.setUserId(user.getId());
            donation.setDonationDate(Dynamics.getDate());
            if (donationInput.getLocationId() != null) {
                donation.setLocationId(donationInput.getLocationId());
            }

            donation = donationRepo.save(donation);
            donation.setStatus("hasn't processed yet...");

            Map<String, Object> card = new HashMap<>();
            card.put("number", donationInput.getCreditCard());
            card.put("exp_month", donationInput.getExpMonth());
            card.put("exp_year", donationInput.getExpYear());
            card.put("cvc", donationInput.getCvc());
            Map<String, Object> params = new HashMap<>();
            params.put("card", card);

            Token token = Token.create(params);

            if (user.getStripeUserId() != null &&
                    !user.getStripeUserId().equals("")) {
                try {
                    Customer oldCustomer = Customer.retrieve(user.getStripeUserId());
                    oldCustomer.delete();
                } catch (Exception e) {
                    log.info("stale stripe customer id");
                }
            }

            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("email", donationInput.getEmail());
            customerParams.put("source", token.getId());
            Customer customer = com.stripe.model.Customer.create(customerParams);

            log.info(customer.getDefaultSource());

            user.setStripeUserId(customer.getId());
            userRepo.update(user);

            Long amountInCents = donationInput.getAmount().multiply(new BigDecimal(100)).longValue();
            log.info("amount -> " + amountInCents);

            String nickname = getDescription(donationInput);
            log.info("nickname " + nickname);

            Boolean chargeSuccess = false;
            Boolean subscriptionSuccess = false;

            if (donationInput.isRecurring()) {

                DynamicsPrice storedPrice = stripeRepo.getPriceAmount(donationInput.getAmount());

                if (storedPrice != null) {

                    Price price =  com.stripe.model.Price.retrieve(storedPrice.getStripeId());
                    if (price == null) {
                        generateStripePrice(amountInCents, storedPrice, donation);
                        storedPrice = stripeRepo.getPrice(storedPrice.getId());
                    }
                    subscriptionSuccess = createSubscription(donation, storedPrice, customer);
                }

                if (storedPrice == null) {

                    DynamicsPrice dynamicsPrice = new DynamicsPrice();
                    dynamicsPrice.setAmount(donationInput.getAmount());
                    dynamicsPrice.setNickname(nickname);

                    Map<String, Object> productParams = new HashMap<>();
                    productParams.put("name", dynamicsPrice.getNickname());

                    com.stripe.model.Product stripeProduct = com.stripe.model.Product.create(productParams);

                    DynamicsProduct dynamicsProduct = new DynamicsProduct();
                    dynamicsProduct.setNickname(dynamicsPrice.getNickname());
                    dynamicsProduct.setStripeId(stripeProduct.getId());
                    DynamicsProduct savedProduct = stripeRepo.saveProduct(dynamicsProduct);

                    Price stripePrice = genStripeReccurringPrice(amountInCents, dynamicsPrice, stripeProduct);
                    if (stripePrice == null) {
                        return donation;
                    }

                    dynamicsPrice.setStripeId(stripePrice.getId());
                    dynamicsPrice.setProductId(savedProduct.getId());
                    DynamicsPrice savedPrice = stripeRepo.savePrice(dynamicsPrice);

                    subscriptionSuccess = createSubscription(donation, savedPrice, customer);

                }
            }

            if (!donationInput.isRecurring()) {
                //                log.info("token : " + token.toString());
                //                log.info("customer : " + customer.toString());
                Map<String, Object> chargeParams = new HashMap<>();
                chargeParams.put("amount", amountInCents);
                chargeParams.put("customer", customer.getId());
                chargeParams.put("card", token.getCard().getId());
                chargeParams.put("currency", "usd");

                com.stripe.model.Charge charge = com.stripe.model.Charge.create(chargeParams);

                donation.setChargeId(charge.getId());
                donationRepo.update(donation);

                user.setStripeUserId(customer.getId());
                userRepo.update(user);

                chargeSuccess = true;
            }

            if (chargeSuccess || subscriptionSuccess) {
                if (donationInput.getLocationId() != null) {
                    Location location = locationRepo.get(donationInput.getLocationId());
                    donation.setLocationId(location.getId());
                    donation.setLocation(location);
                }

                donation.setUser(user);
                donation.setProcessed(true);
                donation.setStatus("Successfully processed donation!");
                donationRepo.update(donation);
            }

            if(!chargeSuccess && !subscriptionSuccess){
                donation.setStatus("We need to adjust something. Nothing was charged. Please try again or contact someone");
            }

        }catch(StripeException ex){
            donation.setStatus(ex.getMessage());
        }

        return donation;
    }

    private Price generateStripePrice(Long amountInCents, DynamicsPrice storedPrice, Donation donation) throws StripeException {
        DynamicsProduct dynamicsProduct = stripeRepo.getProduct(storedPrice.getProductId());
        Map<String, Object> productParams = new HashMap<>();
        productParams.put("name", storedPrice.getNickname());

        com.stripe.model.Product stripeProduct = com.stripe.model.Product.create(productParams);

        dynamicsProduct.setStripeId(stripeProduct.getId());
        stripeRepo.updateProduct(dynamicsProduct);

        Map<String, Object> recurring = new HashMap<>();
        recurring.put("interval", "month");

        Map<String, Object> priceParams = new HashMap<>();
        priceParams.put("product", stripeProduct.getId());
        priceParams.put("unit_amount", amountInCents);
        priceParams.put("currency", "usd");
        priceParams.put("recurring", recurring);

        Price stripePrice = Price.create(priceParams);
        storedPrice.setStripeId(stripePrice.getId());
        stripeRepo.updatePrice(storedPrice);

        return stripePrice;
    }

    private String getDescription(DonationInput donationInput){
        String reoccurring = donationInput.isRecurring() ? "Month" : "";
        String location = "";
        if(donationInput.getLocationId() != null){
            Location storedLocation = locationRepo.get(donationInput.getLocationId());
            location = storedLocation.getName();
        }
        return "$" + donationInput.getAmount() + " " + reoccurring + " " +  location;
    }


    private Price genStripeReccurringPrice(Long amountInCents, DynamicsPrice dynamicsPrice, Product stripeProduct) throws StripeException {
        Map<String, Object> recurring = new HashMap<>();
        recurring.put("interval", dynamicsPrice.getFrequency());

        Map<String, Object> priceParams = new HashMap<>();
        priceParams.put("product", stripeProduct.getId());
        priceParams.put("unit_amount", amountInCents);
        priceParams.put("currency", dynamicsPrice.getCurrency());
        priceParams.put("recurring", recurring);

        Price stripePrice = Price.create(priceParams);
        return stripePrice;
    }

    private boolean createSubscription(Donation donation, DynamicsPrice dynamicsPrice, Customer customer) throws StripeException {
        Map<String, Object> itemParams = new HashMap<>();
        itemParams.put("price", dynamicsPrice.getStripeId());

        Map<String, Object> itemsParams = new HashMap<>();
        itemsParams.put("0", itemParams);

        Map<String, Object> subscriptionParams = new HashMap<>();
        subscriptionParams.put("customer", customer.getId());
        subscriptionParams.put("items", itemsParams);

        com.stripe.model.Subscription subscription = com.stripe.model.Subscription.create(subscriptionParams);

        donation.setSubscriptionId(subscription.getId());
        donationRepo.update(donation);

        return true;
    }

    public String select(ModelMap modelMap, RedirectAttributes redirect){
        if(!authService.isAuthenticated()){
            redirect.addFlashAttribute("message", "Please signin to continue");
            return "redirect:/";
        }
        List<DynamicsPrice> dynamicsPrices = stripeRepo.getList();
        modelMap.put("dynamicsPrices", dynamicsPrices);
        return "price/select";
    }

    public String upgrade(ModelMap modelMap, RedirectAttributes redirect){
        if(!authService.isAuthenticated()){
            redirect.addFlashAttribute("message", "Please signin to continue");
            return "redirect:/";
        }

        User user = authService.getUser();
        List<DynamicsPrice> dynamicsPrices = stripeRepo.getList();

        modelMap.put("user", user);
        modelMap.put("dynamicsPrices", dynamicsPrices);

        return "price/upgrade";
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

        DynamicsPrice dynamicsPrice = stripeRepo.getPrice(id);
        modelMap.put("dynamicsPrice", dynamicsPrice);

        return "dynamicsPrice/confirm";
    }


    public String cancel(String subscriptionId){
        try{

            Stripe.apiKey = persistenceService.getApiKey();
            com.stripe.model.Subscription subscription = com.stripe.model.Subscription.retrieve(subscriptionId);
            subscription.cancel();

            Donation donation = donationRepo.get(subscriptionId);
            donation.setCancelled(true);
            donationRepo.update(donation);

        }catch(Exception ex){
            ex.printStackTrace();
        }

        return Constants.GAINING;
    }

    public String cancel(Long locationId, String subscriptionId){
        try{

            Stripe.apiKey = persistenceService.getApiKey(locationId);
            com.stripe.model.Subscription subscription = com.stripe.model.Subscription.retrieve(subscriptionId);
            subscription.cancel();

            Donation donation = donationRepo.get(subscriptionId);
            donation.setCancelled(true);
            donationRepo.update(donation);

        }catch(Exception ex){
            ex.printStackTrace();
        }

        return Constants.GAINING;
    }

    public String list(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/unauthorized";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }
        List<DynamicsPrice> dynamicsPrices = stripeRepo.getList();
        modelMap.put("dynamicsPrices", dynamicsPrices);
        return "price/index";
    }

    public String create(){
        if(!authService.isAuthenticated()){
            return "redirect:/unauthorized";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }
        return "price/create";
    }


    public String save(DynamicsPrice dynamicsPrice, RedirectAttributes redirectAttributes){
        if(!authService.isAuthenticated()){
            return "redirect:/unauthorized";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }
        if(dynamicsPrice.getAmount().multiply(new BigDecimal(100)).longValue() >= 1000){
            redirectAttributes.addFlashAttribute("message", "You just entered an amount larger than $1000.00");
            return "redirect:/donate/list";
        }
        if(dynamicsPrice.getNickname().equals("")){
            redirectAttributes.addFlashAttribute("message", "blank nickname");
            return "redirect:/donate/list";
        }

        try {

            Stripe.apiKey = persistenceService.getApiKey();

            Map<String, Object> productParams = new HashMap<>();
            productParams.put("name", dynamicsPrice.getNickname());
            com.stripe.model.Product stripeProduct = com.stripe.model.Product.create(productParams);

            DynamicsProduct dynamicsProduct = new DynamicsProduct();
            dynamicsProduct.setNickname(dynamicsPrice.getNickname());
            dynamicsProduct.setStripeId(stripeProduct.getId());
            DynamicsProduct savedDynamicsProduct = stripeRepo.saveProduct(dynamicsProduct);


            Map<String, Object> priceParams = new HashMap<>();
            priceParams.put("product", stripeProduct.getId());
            priceParams.put("nickname", dynamicsPrice.getNickname());
            priceParams.put("interval", dynamicsPrice.getFrequency());
            priceParams.put("currency", dynamicsPrice.getCurrency());
            priceParams.put("amount", dynamicsPrice.getAmount());
            com.stripe.model.Price stripePrice = com.stripe.model.Price.create(priceParams);

            dynamicsPrice.setStripeId(stripePrice.getId());
            dynamicsPrice.setProductId(savedDynamicsProduct.getId());
            stripeRepo.savePrice(dynamicsPrice);

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return "redirect:/dynamicsPrice/list";
    }

    public String delete(Long id, RedirectAttributes redirect){
        if(!authService.isAuthenticated()){
            return "redirect:/unauthorized";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }

        DynamicsPrice dynamicsPrice = stripeRepo.getPrice(id);
        DynamicsProduct dynamicsProduct = stripeRepo.getProduct(dynamicsPrice.getProductId());

        try{
            com.stripe.model.Plan price = com.stripe.model.Plan.retrieve(dynamicsPrice.getStripeId());
            price.delete();
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            com.stripe.model.Product product = com.stripe.model.Product.retrieve(dynamicsProduct.getStripeId());
            product.delete();
        }catch(Exception e){
            e.printStackTrace();
        }

        List<User> users = userRepo.getPriceList(dynamicsPrice.getId());
        for(User user : users){
            userRepo.removePrice(user.getId());
        }

        stripeRepo.deletePrice(dynamicsPrice.getId());
        stripeRepo.deleteProduct(dynamicsProduct.getId());

        return "redirect:/price/list";
    }

    public String cleanup(){
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator()){
            return "redirect:/";
        }
        try {

            Stripe.apiKey = persistenceService.getApiKey();

            Map<String, Object> params = new HashMap<>();
            params.put("limit", 100);
            PlanCollection priceCollection = com.stripe.model.Plan.list(params);
            List<com.stripe.model.Plan> plans = priceCollection.getData();
            for(Plan plan: plans){
                plan.delete();
            }

            ProductCollection productCollection = com.stripe.model.Product.list(params);
            List<com.stripe.model.Product> products = productCollection.getData();
            for(com.stripe.model.Product product: products){
                product.delete();
            }

            CustomerCollection customerCollection = com.stripe.model.Customer.list(params);
            List<com.stripe.model.Customer> customers = customerCollection.getData();
            for(Customer customer: customers){
                customer.delete();
            }

            SubscriptionCollection subscriptionCollection = com.stripe.model.Subscription.list(params);
            List<com.stripe.model.Subscription> subscriptions = subscriptionCollection.getData();
            for(com.stripe.model.Subscription subscription: subscriptions){
                subscription.cancel();
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return "redirect:/";
    }

    public String momentum(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }
        List<Donation> donations = donationRepo.getList();
        BigDecimal sum = new BigDecimal(0);
        for(Donation donation: donations){
            sum = sum.add(donation.getAmount());
        }
        modelMap.put("sum", sum);
        modelMap.put("donations", donations);
        return "momentum/index";
    }
}
