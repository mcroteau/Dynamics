package dynamics.gain.web;

import dynamics.gain.common.Utils;
import dynamics.gain.model.DailyCount;
import dynamics.gain.model.Location;
import dynamics.gain.model.User;
import dynamics.gain.repository.DailyRepo;
import dynamics.gain.repository.LocationRepo;
import dynamics.gain.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
public class DailyController {

    @Autowired
    private AuthService authService;

    @Autowired
    private DailyRepo dailyRepo;

    @Autowired
    private LocationRepo locationRepo;


    @RequestMapping(value="/daily/entry/{id}", method=RequestMethod.GET)
    protected String entry(ModelMap model, @PathVariable String id){

        if(!authService.isAuthenticated()){
            return "redirect:/signin";
        }

        Location location = locationRepo.get(Long.parseLong(id));
        model.addAttribute("location", location);
        return "daily/entry";
    }



    @RequestMapping(value="/daily/edit/{id}", method=RequestMethod.GET)
    protected String edit(ModelMap model, @PathVariable String id){

        if(!authService.isAuthenticated()){
            return "redirect:/signin";
        }

        DailyCount dailyCount = dailyRepo.get(Long.parseLong(id));
        model.addAttribute("dailyCount", dailyCount);

        return "daily/edit";
    }

    @RequestMapping(value="/daily/save", method=RequestMethod.POST)
    protected String save(@ModelAttribute("dailyCount") DailyCount dailyCount,
                          RedirectAttributes redirect){

        if(!authService.isAuthenticated()){
            return "redirect:/signin";
        }

        User user = authService.getUser();
        dailyCount.setAccountId(user.getId());

        long date = Utils.getToday();
        dailyCount.setDateEntered(date);

        if(Objects.isNull(dailyCount.getCount())){
            dailyCount.setCount(0);
        }

        dailyRepo.save(dailyCount);
        redirect.addFlashAttribute("message", "Successfully entered numbers...");
        return "redirect:/locations";
    }


    @RequestMapping(value="/daily/update", method=RequestMethod.POST)
    protected String update(@ModelAttribute("dailyCount") DailyCount dailyCount,
                            RedirectAttributes redirect){

        if(!authService.isAuthenticated()){
            return "redirect:/signin";
        }

        DailyCount existingCount = dailyRepo.get(dailyCount.getId());
        dailyCount.setDateEntered(existingCount.getDateEntered());
        dailyRepo.update(dailyCount);

        redirect.addFlashAttribute("message", "Successfully updated numbers...");
        return "redirect:/locations";
    }

}
