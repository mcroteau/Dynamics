package dynamics.gain.web;

import com.google.gson.Gson;
import dynamics.gain.model.Location;
import dynamics.gain.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LocationController {

    Gson gson = new Gson();

    @Autowired
    LocationService locationService;

    @GetMapping(value="/location/create")
    public String index(ModelMap modelMap){
        return locationService.create(modelMap);
    }

    @GetMapping(value="/location/{uri}")
    public String index(ModelMap modelMap,
                        @PathVariable String uri){
        return locationService.index(uri, modelMap);
    }

    @PostMapping(value="/location/save")
    protected String save(@ModelAttribute("location") Location location,
                          RedirectAttributes redirect){
        return locationService.save(location, redirect);
    }

    @GetMapping(value="/admin/location/list")
    public String getProjects(ModelMap modelMap){
        return locationService.getLocations(modelMap);
    }

    @GetMapping(value="/location/edit/{id}")
    public String getEdit(ModelMap modelMap,
                              @PathVariable Long id){
        return locationService.getEdit(id, modelMap);
    }

    @PostMapping(value="/location/update")
    protected String update(@ModelAttribute("location") Location location,
                            RedirectAttributes redirect){
        return locationService.update(location, redirect);
    }

    @PostMapping(value="/location/delete/{id}")
    protected String delete(@PathVariable Long id,
                            RedirectAttributes redirect){
        return locationService.delete(id, redirect);
    }

}
