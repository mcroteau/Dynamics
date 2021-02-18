package dynamics.gain.service;

import dynamics.gain.common.Constants;
import dynamics.gain.model.Location;
import dynamics.gain.model.Town;
import dynamics.gain.model.User;
import dynamics.gain.repository.DailyRepo;
import dynamics.gain.repository.LocationRepo;
import dynamics.gain.repository.TownRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class TownService {

    @Autowired
    TownRepo townRepo;

    @Autowired
    LocationRepo locationRepo;

    @Autowired
    DailyRepo dailyRepo;

    @Autowired
    AuthService authService;

    public String getPermission(String id){
        return Constants.TOWN_MAINTENANCE + id;
    }

    public String index(String uri, ModelMap modelMap) {
        Town town = townRepo.get(uri);
        List<Location> locations = locationRepo.getList(town.getId());

        long sum = 0;
        for(Location location: locations){
            sum = sum + location.getCount();
        }
        String count = NumberFormat.getInstance(Locale.US).format(sum);

        modelMap.put("count", count);
        modelMap.put("town", town);
        modelMap.put("locations", locations);

        return "town/index";
    }

    public String create(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        return "town/create";
    }

    public String save(Town town, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        if(town.getName().equals("")){
            redirect.addFlashAttribute("message", "Please give your web town a name...");
            return "redirect:/admin/towns/create";
        }

        townRepo.save(town);
        return "redirect:/admin/towns";
    }

    public String getEdit(Long id, ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }

        Town town = townRepo.get(id);
        modelMap.put("town", town);

        return "town/edit";
    }

    public String update(Town town, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator()){
            return "redirect:/";
        }

        if(town.getName().equals("")){
            redirect.addFlashAttribute("message", "Please give your web town a name...");
            return "redirect:/admin/towns/edit/" + town.getId();
        }

        townRepo.update(town);

        redirect.addFlashAttribute("message", "Successfully updated town");
        return "redirect:/admin/towns/edit/" + town.getId();
    }

    public String getTowns(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        if(!authService.isAdministrator()){
            return "redirect:/";
        }

        List<Town> towns = townRepo.getList();
        modelMap.put("towns", towns);

        return "town/list";
    }

    public String delete(Long id, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }

        List<Location> locations = locationRepo.getList(id);
        for(Location location: locations){
            dailyRepo.deleteCounts(location.getId());
        }
        locationRepo.deleteLocations(id);
        townRepo.delete(id);
        redirect.addFlashAttribute("message", "Successfully deleted town.");

        return "redirect:/admin/towns";
    }

}
