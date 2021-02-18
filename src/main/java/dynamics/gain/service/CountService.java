package dynamics.gain.service;

import dynamics.gain.common.Constants;
import dynamics.gain.common.App;
import dynamics.gain.model.DailyCount;
import dynamics.gain.model.Location;
import dynamics.gain.model.User;
import dynamics.gain.repository.DailyRepo;
import dynamics.gain.repository.LocationRepo;
import dynamics.gain.repository.TownRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class CountService {

    private static final Logger log = Logger.getLogger(CountService.class);

    @Autowired
    AuthService authService;

    @Autowired
    DailyRepo dailyRepo;

    @Autowired
    LocationRepo locationRepo;


    public String entry(Long id, ModelMap modelMap) {

        if(!authService.isAuthenticated()){
            return "redirect:/signin";
        }

        if(!authService.isAdministrator() &&
                !authService.hasRole(Constants.SUPER_DUPER)){
            return Constants.UNAUTHORIZED_REDIRECT;
        }

        User user = authService.getUser();

        DailyCount dailyCount = dailyRepo.getCount(id, App.getToday());
        log.info(dailyCount);
        if(dailyCount != null){
            log.info(dailyCount.getCount());
        }
        DailyCount storedCount = dailyCount;
        if(dailyCount == null){
            dailyCount = new DailyCount();
            dailyCount.setCount(0);
            dailyCount.setLocationId(id);
            dailyCount.setUserId(user.getId());
            dailyCount.setDateEntered(App.getToday());
            storedCount = dailyRepo.save(dailyCount);
        }
        
        Location location = locationRepo.get(id);
        modelMap.addAttribute("location", location);
        modelMap.addAttribute("dailyCount", storedCount);

        return "count/index";
    }


    public String update(DailyCount dailyCount, RedirectAttributes redirect) {

        if(!authService.isAuthenticated()){
            return "redirect:/signin";
        }

        if(!authService.isAdministrator() &&
                !authService.hasRole(Constants.SUPER_DUPER)){
            return Constants.UNAUTHORIZED_REDIRECT;
        }

        User user = authService.getUser();
        log.info("count : " + dailyCount.getCount());

        dailyCount.setUserId(user.getId());
        dailyRepo.update(dailyCount);

        Location location = locationRepo.get(dailyCount.getLocationId());
        location.setCount(dailyCount.getCount());
        locationRepo.update(location);

        redirect.addFlashAttribute("message", "Successfully updated count...");
        return "redirect:/admin/count/" + location.getId();
    }
}
