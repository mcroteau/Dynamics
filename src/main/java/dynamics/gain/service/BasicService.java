package dynamics.gain.service;

import dynamics.gain.model.Location;
import dynamics.gain.model.Town;
import dynamics.gain.repository.LocationRepo;
import dynamics.gain.repository.TownRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
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


    public String index() {
        if(authService.isAuthenticated()){
            long id =  authService.getUser().getId();
            return "redirect:/user/edit/" + id;
        }
        return "redirect:/home";
    }

    public String home(ModelMap modelMap){
        int count = 0;
        List<Location> locations = locationRepo.getList();
        for(Location location: locations){
            count = count + location.getCount();
        }

        List<Town> towns = townRepo.getList();

        modelMap.put("towns", towns);
        modelMap.put("count", count);
        return "home";
    }

    public String towns(ModelMap modelMap){
        List<Town> towns = townRepo.getList();
        modelMap.put("towns", towns);
        return "basic/towns";
    }

    public String locations(ModelMap modelMap){
        List<Town> locations = new ArrayList();
        List<Town> towns = townRepo.getList();

        for(Town town: towns){
            List<Location> townLocations = locationRepo.getList(town.getId());
            town.setLocations(townLocations);
            locations.add(town);
        }

        modelMap.put("locations", locations);
        return "basic/locations";
    }


    public String showSignin() {
        if(authService.isAuthenticated()){
            return "redirect:/";
        }
        return "basic/signin";
    }

    public String beginReport() {
        phoneService.support("Dynamics +Gain issue");
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
