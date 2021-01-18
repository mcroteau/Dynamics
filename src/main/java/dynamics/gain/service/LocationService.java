package dynamics.gain.service;

import dynamics.gain.common.Constants;
import dynamics.gain.common.Utils;
import dynamics.gain.model.*;
import dynamics.gain.repository.LocationRepo;
import dynamics.gain.repository.UserRepo;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class LocationService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    LocationRepo locationRepo;

    @Autowired
    AuthService authService;

    @Autowired
    PhoneService phoneService;

    @Autowired
    EmailService emailService;

    public String getPermission(String id){
        return Constants.PROJECT_MAINTENANCE + id;
    }

    public String index(String uri, ModelMap modelMap) {
        Location location = locationRepo.get(uri);
        modelMap.put("location", location);
        return "location/index";
    }

    public String create(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        return "location/create";
    }

    public String getLocations(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        if(!authService.isAdministrator()){
            return "redirect:/";
        }

        List<Location> locations = locationRepo.getList();
        modelMap.addAttribute("locations", locations);

        return "location/list";
    }

    public String save(Location location, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        if(location.getName().equals("")){
            redirect.addFlashAttribute("message", "Please give your web location a name...");
            return "redirect:/location/create";
        }

        User authdUser = authService.getUser();
        location.setUserId(authdUser.getId());

        Location savedLocation = locationRepo.save(location);
        String permission = getPermission(Long.toString(savedLocation.getId()));
        userRepo.savePermission(authdUser.getId(), permission);

        return "redirect:/location/overview";
    }

    public String edit(Long id, ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        String permission = getPermission(Long.toString(id));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/";
        }

        Location location = locationRepo.get(id);
        modelMap.addAttribute("location", location);

        return "location/edit";
    }

    public String getEdit(Long id, ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        String permission = getPermission(Long.toString(id));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/unauthorized";
        }

        Location location = locationRepo.get(id);
        modelMap.put("location", location);

        return "location/edit";
    }

    public String update(Location location, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        String permission = getPermission(Long.toString(location.getId()));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/";
        }

        if(location.getName().equals("")){
            redirect.addFlashAttribute("message", "Please give your web location a name...");
            return "redirect:/location/edit/" + location.getId();
        }

        redirect.addFlashAttribute("message", "Successfully updated location");
        return "redirect:/location/edit/" + location.getId();
    }

    public String delete(Long id, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        String permission = getPermission(Long.toString(id));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/unauthorized";
        }

        locationRepo.delete(id);
        redirect.addFlashAttribute("message", "Successfully deleted location.");

        return "redirect:/location/overview";
    }


}
