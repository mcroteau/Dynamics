package dynamics.gain.service;

import dynamics.gain.model.Location;
import dynamics.gain.model.Town;
import dynamics.gain.repository.LocationRepo;
import dynamics.gain.repository.TownRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Service
public class BasicService {

    @Autowired
    TownRepo townRepo;

    @Autowired
    LocationRepo locationRepo;

    @Autowired
    AuthService authService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private EmailService emailService;

    public String home(ModelMap modelMap){
        int count = 0;
        List<Location> locations = locationRepo.getList();
        for(Location location: locations){
            count = count + location.getCount();
        }

        List<Town> towns = townRepo.getList();

        modelMap.put("towns", towns);
        modelMap.put("count", count);
        return "index";
    }

    public String index() {
        if(authService.isAuthenticated()){
            return "redirect:/project/overview";
        }
        return "redirect:/home";
    }

    public String showSignin() {
        if(authService.isAuthenticated()){
            return "redirect:/";
        }
        return "basic/signin";
    }

    public String beginReport() {
        phoneService.support("Okay issue");
        return "basic/report";
    }

    public String reportIssue(String email, String issue, ModelMap modelMap, RedirectAttributes redirect) {

        if (email.equals("")) {
            redirect.addFlashAttribute("error", "Please enter a valid email address");
            return "redirect:/issues/report";
        }

        if (issue.equals("")) {
            redirect.addFlashAttribute("error", "Issue was left black, please tell us what happened.");
            return "redirect:/issues/report";
        }

        StringBuffer sb = new StringBuffer();
        sb.append(email);
        sb.append("<br/>");
        sb.append(issue);
        emailService.send("croteau.mike@gmail.com", "Okay!", sb.toString());

        modelMap.addAttribute("message", "Thank you. Issue has been reported.");
        return "basic/success";
    }

}
