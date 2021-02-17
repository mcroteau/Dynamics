package dynamics.gain.service;

import dynamics.gain.common.Constants;
import dynamics.gain.common.Utils;
import dynamics.gain.model.*;
import dynamics.gain.repository.DailyRepo;
import dynamics.gain.repository.LocationRepo;
import dynamics.gain.repository.TownRepo;
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
    TownRepo townRepo;

    @Autowired
    LocationRepo locationRepo;

    @Autowired
    DailyRepo dailyRepo;

    @Autowired
    AuthService authService;

    @Autowired
    PhoneService phoneService;

    @Autowired
    EmailService emailService;

    @Autowired
    LightService lightService;

    public String index(String uri, ModelMap modelMap) {
        Location location = locationRepo.get(uri);
        modelMap.put("location", location);
        return "location/index";
    }

    public String create(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Constants.SUPER_DUPER)){
            return "redirect:/";
        }
        List<Town> towns = townRepo.getList();
        modelMap.put("towns", towns);
        return "location/create";
    }

    public String getLocations(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Constants.SUPER_DUPER)){
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
        if(!authService.isAdministrator() &&
                !authService.hasRole(Constants.SUPER_DUPER)){
            return "redirect:/";
        }

        if(location.getName().equals("")){
            redirect.addFlashAttribute("message", "Please give your web location a name...");
            return "redirect:/admin/locations/create";
        }

        Location savedLocation = locationRepo.save(location);
        return "redirect:/admin/locations/edit/" + savedLocation.getId();
    }

    public String getEdit(Long id, ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Constants.SUPER_DUPER)){
            return "redirect:/";
        }

        List<Town> towns = townRepo.getList();
        Location location = locationRepo.get(id);

        String devKey = lightService.get(Constants.ORGANIZATIONS, "dev." + location.getId());
        String liveKey = lightService.get(Constants.ORGANIZATIONS, "live." + location.getId());

        location.setDevKey(devKey);
        location.setLiveKey(liveKey);

        modelMap.put("towns", towns);
        modelMap.put("location", location);

        return "location/edit";
    }

    public String update(Location location, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Constants.SUPER_DUPER)){
            return "redirect:/";
        }

        if(location.getName().equals("")){
            redirect.addFlashAttribute("message", "Please give your location a name...");
            return "redirect:/admin/locations/edit/" + location.getId();
        }

        if(location.getDevKey() != null &&
                location.getLiveKey() != null){
            String[] keys = {"dev." + location.getId(), "live." + location.getId()};
            String[] values = { location.getDevKey(), location.getLiveKey() };
            lightService.write(Constants.ORGANIZATIONS, keys, values);
        }
        locationRepo.update(location);

        redirect.addFlashAttribute("message", "Successfully updated location");
        return "redirect:/admin/locations/edit/" + location.getId();
    }

    public String delete(Long id, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }

        dailyRepo.deleteCounts(id);
        locationRepo.delete(id);
        redirect.addFlashAttribute("message", "Successfully deleted location.");

        return "redirect:/admin/locations";
    }

}
