package dynamics.gain.web;

import dynamics.gain.model.DailyCount;
import dynamics.gain.service.CountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CountController {

    @Autowired
    CountService countService;

    @RequestMapping(value="/admin/count/{id}", method=RequestMethod.GET)
    protected String entry(ModelMap modelMap, @PathVariable Long id){
        return countService.entry(id, modelMap);
    }

    @RequestMapping(value="/admin/count/update", method=RequestMethod.POST)
    protected String update(@ModelAttribute("dailyCount") DailyCount dailyCount,
                            RedirectAttributes redirect){
        return countService.update(dailyCount, redirect);
    }

}
