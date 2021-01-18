package dynamics.gain.service;

import dynamics.gain.model.Location;
import dynamics.gain.model.Town;
import dynamics.gain.repository.LocationRepo;
import dynamics.gain.repository.TownRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;

@Service
public class TownService {

    @Autowired
    TownRepo townRepo;

    @Autowired
    LocationRepo locationRepo;

    public String index(String uri, ModelMap modelMap) {
        Town town = townRepo.get(uri);
        List<Location> locations = locationRepo.getList(town.getId());
        modelMap.put("town", town);
        modelMap.put("locations", locations);

        return "town/index";
    }
}
