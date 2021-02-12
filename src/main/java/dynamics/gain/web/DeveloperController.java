package dynamics.gain.web;

import dynamics.gain.model.User;
import dynamics.gain.service.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DeveloperController {

    @Autowired
    DeveloperService developerService;

    @GetMapping(value="/developer/setup")
    public String setup(ModelMap modelMap){
        return developerService.setup(modelMap);
    }

    @PostMapping(value="/developer/save")
    public String save(ModelMap modelMap,
                       @ModelAttribute("user") User user){
        return developerService.save(user, modelMap);
    }

}
