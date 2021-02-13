package dynamics.gain.service;

import dynamics.gain.common.Constants;
import dynamics.gain.common.Utils;
import dynamics.gain.model.DailyCount;
import dynamics.gain.model.Location;
import dynamics.gain.model.User;
import dynamics.gain.repository.DailyRepo;
import dynamics.gain.repository.LocationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

public class DailyService {

    @Autowired
    private AuthService authService;

    @Autowired
    private DailyRepo dailyRepo;

    @Autowired
    private LocationRepo locationRepo;


    public String entry(Long id, ModelMap modelMap) {

        if(!authService.isAuthenticated()){
            return "redirect:/signin";
        }

        if(!authService.hasRole(Constants.SUPER_DUPER)){
            return Constants.UNAUTHORIZED_REDIRECT;
        }

        Location location = locationRepo.get(id);
        modelMap.addAttribute("location", location);
        return "daily/entry";
    }

    public String edit(Long id, ModelMap modelMap) {

        if(!authService.isAuthenticated()){
            return "redirect:/signin";
        }

        if(!authService.hasRole(Constants.SUPER_DUPER)){
            return Constants.UNAUTHORIZED_REDIRECT;
        }

        DailyCount dailyCount = dailyRepo.get(id);
        modelMap.addAttribute("dailyCount", dailyCount);

        return "daily/edit";
    }

    public String save(DailyCount dailyCount, RedirectAttributes redirect) {

        if(!authService.isAuthenticated()){
            return "redirect:/signin";
        }

        if(!authService.hasRole(Constants.SUPER_DUPER)){
            return Constants.UNAUTHORIZED_REDIRECT;
        }

        User user = authService.getUser();
        dailyCount.setAccountId(user.getId());

        long date = Utils.getToday();
        dailyCount.setDateEntered(date);

        if(Objects.isNull(dailyCount.getCount())){
            dailyCount.setCount(0);
        }

        if(dailyCount.getCount() != 0){
            Location location = locationRepo.get(dailyCount.getLocationId());
            location.setCount(dailyCount.getCount());
            locationRepo.update(location);
        }

        dailyRepo.save(dailyCount);
        redirect.addFlashAttribute("message", "Successfully entered numbers...");
        return "redirect:/admin/locations";
    }

    public String update(DailyCount dailyCount, RedirectAttributes redirect) {

        if(!authService.isAuthenticated()){
            return "redirect:/signin";
        }

        if(!authService.hasRole(Constants.SUPER_DUPER)){
            return Constants.UNAUTHORIZED_REDIRECT;
        }

        if(Objects.isNull(dailyCount.getCount())){
            dailyCount.setCount(0);
        }

        DailyCount existingCount = dailyRepo.get(dailyCount.getId());
        dailyCount.setDateEntered(existingCount.getDateEntered());
        dailyRepo.update(dailyCount);

        if(dailyCount.getCount() != 0){
            Location location = locationRepo.get(dailyCount.getLocationId());
            location.setCount(dailyCount.getCount());
            locationRepo.update(location);
        }

        redirect.addFlashAttribute("message", "Successfully updated numbers...");
        return "redirect:/admin/locations";
    }
}
