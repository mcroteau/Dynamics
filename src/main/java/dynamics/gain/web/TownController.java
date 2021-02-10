package dynamics.gain.web;

import dynamics.gain.model.Town;
import dynamics.gain.service.TownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TownController {

    @Autowired
    TownService townService;

    @GetMapping(value="/town/{uri}")
    public String index(ModelMap modelMap,
                        @PathVariable String uri){
        return townService.index(uri, modelMap);
    }

    @GetMapping(value="/town/create")
    public String index(ModelMap modelMap){
        return townService.create(modelMap);
    }

    @PostMapping(value="/town/save")
    protected String save(@ModelAttribute("town") Town town,
                          RedirectAttributes redirect){
        return townService.save(town, redirect);
    }

    @GetMapping(value="/admin/town/list")
    public String getProjects(ModelMap modelMap){
        return townService.getTowns(modelMap);
    }

    @GetMapping(value="/town/edit/{id}")
    public String getEdit(ModelMap modelMap,
                          @PathVariable Long id){
        return townService.getEdit(id, modelMap);
    }

    @PostMapping(value="/town/update")
    protected String update(@ModelAttribute("town") Town town,
                            RedirectAttributes redirect){
        return townService.update(town, redirect);
    }

    @PostMapping(value="/town/delete/{id}")
    protected String delete(@PathVariable Long id,
                            RedirectAttributes redirect){
        return townService.delete(id, redirect);
    }


}
