package dynamics.gain.web;

import dynamics.gain.service.TownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TownController {

    @Autowired
    TownService townService;

    @GetMapping(value="/town/{uri}")
    public String index(ModelMap modelMap,
                        @PathVariable String uri){
        return townService.index(uri, modelMap);
    }


}
