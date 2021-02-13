package dynamics.gain.web;

import dynamics.gain.common.Constants;
import dynamics.gain.common.Utils;
import dynamics.gain.model.DailyCount;
import dynamics.gain.model.Location;
import dynamics.gain.model.User;
import dynamics.gain.repository.DailyRepo;
import dynamics.gain.repository.LocationRepo;
import dynamics.gain.service.AuthService;
import dynamics.gain.service.DailyService;
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
    DailyService dailyService;

    @RequestMapping(value="/admin/count/{id}", method=RequestMethod.GET)
    protected String entry(ModelMap modelMap, @PathVariable Long id){
        return dailyService.entry(id, modelMap);
    }

//    @RequestMapping(value="/admin/daily/edit/{id}", method=RequestMethod.GET)
//    protected String edit(ModelMap modelMap, @PathVariable Long id){
//        return dailyService.edit(id, modelMap);
//    }
//    @RequestMapping(value="/admin/daily/save", method=RequestMethod.POST)
//    protected String save(@ModelAttribute("dailyCount") DailyCount dailyCount,
//                          RedirectAttributes redirect){
//        return dailyService.save(dailyCount, redirect);
//    }

    @RequestMapping(value="/admin/count/update", method=RequestMethod.POST)
    protected String update(@ModelAttribute("dailyCount") DailyCount dailyCount,
                            RedirectAttributes redirect){
        return dailyService.update(dailyCount, redirect);
    }

}
