package dynamics.gain.service;

import dynamics.gain.common.App;
import dynamics.gain.model.User;
import dynamics.gain.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
public class DeveloperService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    AuthService authService;

    public String setup(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        User authdUser = authService.getUser();
        modelMap.put("user", authdUser);

        return "developer/setup";
    }

    public String save(User user, ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        String apiKey = App.getRandomString(19);
//        user.setApiKey(apiKey);
        userRepo.update(user);

        User updatedUser = userRepo.get(user.getId());
        modelMap.put("user", updatedUser);

        return "developer/complete";
    }

}
