package dynamics.gain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
public class DonateService {

    private static final String USD = "usd";
    private static final String FREQUENCY = "month";

//    @Autowired
//    DonationRepo donationRepo;

    @Autowired
    AuthService authService;

    @Autowired
    EmailService emailService;

    public String createSubscriptionPlans(HttpServletRequest req, RedirectAttributes redirect){
        if(!authService.isAuthenticated()){
            redirect.addFlashAttribute("Mike, you need to sign in!");
            return "redirect:/";
        }
        if(!authService.isAdministrator()){
            if(!req.getSession().getAttribute("notified").equals("")){
                redirect.addFlashAttribute("Wow, again! Im telling on you... 'Super User!!!! So and so just tried to do something bad!'");
                emailService.send(authService.getUser().getUsername(), "croteau.mike@gmail.com", "Someone did something!", authService.getUser().getUsername());
            }else{
                redirect.addFlashAttribute("Please don't try this again! Ha!");
            }
            return "redirect:/";
        }

        try {

//            if (!moolaRepo.plansExist()) {
//                String[][] plans = {
////                        {"Free", "", "3"},
//                        {"Starter", "40", "9"},
//                        {"It's Business", "79", "19"}
//                };
//
//                for (String[] plan : plans) {
//                    Map<String, Object> productParams = new HashMap<String, Object>()
//                    productParams.put("name", plan[0]);
//                    productParams.put("type", "service");
//                    Product stripeProduct = com.stripe.model.Product.create(stripeProductMap);
//
//                    Map<String, Object> planParams = new HashMap<String, Object>()
//                    planParams.put("product", stripeProduct.getId());
//                    planParams.put("nickname", plan[0]);
//                    planParams.put("interval", FREQUENCY);
//                    planParams.put("currency", USD);
//                    planParams.put("amount", Integer.parseInt(plan[1]));
//                    Plan stripePlan = com.stripe.model.Plan.create(planParams);
//
//                    AppProduct okayProduct = new AppProduct();
//                    okayProduct.setNickname(plan[0]);
//                    okayProduct.setStripeId(stripeProduct.getId());
//                    moolaRepo.saveProduct(okayProduct);
//
//                    AppPlan okayPlan = new AppPlan();
//                    okayPlan.setNickname(plan[0]);
//                    okayPlan.setAmount(Integer.parseInt(plan[1]));
//                    okayPlan.setDescription(plan[2]);
//                    okayPlan.setCurrency(USD);
//
//                }
//            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return "";
    }

}
