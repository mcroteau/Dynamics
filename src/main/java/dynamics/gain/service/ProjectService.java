package dynamics.gain.service;

import dynamics.gain.common.Constants;
import dynamics.gain.common.Utils;
import dynamics.gain.model.*;
import dynamics.gain.repository.ProjectRepo;
import dynamics.gain.repository.UserRepo;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    ProjectRepo projectRepo;

    @Autowired
    AuthService authService;

    @Autowired
    PhoneService phoneService;

    @Autowired
    EmailService emailService;

    public String getPermission(String id){
        return Constants.PROJECT_MAINTENANCE + id;
    }
    private String getPhonePermission(String id){
        return Constants.PHONE_MAINTENANCE + id;
    }
    private String getEmailPermission(String id){
        return Constants.EMAIL_MAINTENANCE + id;
    }

    public String create(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        return "project/create";
    }

    public String index(Long id, ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        String permission = getPermission(Long.toString(id));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/";
        }

        Project project = projectRepo.get(id);
        modelMap.addAttribute("project", project);

        return "project/index";
    }

    public String save(Project project, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        if(project.getName().equals("")){
            redirect.addFlashAttribute("message", "Please give your web project a name...");
            return "redirect:/project/create";
        }

        User authdUser = authService.getUser();
        project.setUserId(authdUser.getId());

        Project savedProject = projectRepo.save(project);
        String permission = getPermission(Long.toString(savedProject.getId()));
        userRepo.savePermission(authdUser.getId(), permission);

        return "redirect:/project/overview";
    }

    public String edit(Long id, ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        String permission = getPermission(Long.toString(id));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/";
        }

        Project project = projectRepo.get(id);
        modelMap.addAttribute("project", project);

        return "project/edit";
    }

    public String getEdit(Long id, ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        String permission = getPermission(Long.toString(id));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/unauthorized";
        }

        Project project = projectRepo.get(id);
        modelMap.put("project", project);

        return "project/edit";
    }

    public String update(Project project, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        String permission = getPermission(Long.toString(project.getId()));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/";
        }

        if(project.getName().equals("")){
            redirect.addFlashAttribute("message", "Please give your web project a name...");
            return "redirect:/project/edit/" + project.getId();
        }

        redirect.addFlashAttribute("message", "Successfully updated project");
        projectRepo.update(project);
        return "redirect:/project/edit/" + project.getId();
    }

    public String delete(Long id, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        String permission = getPermission(Long.toString(id));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/unauthorized";
        }

        projectRepo.delete(id);
        redirect.addFlashAttribute("message", "Successfully deleted project.");

        return "redirect:/project/overview";
    }

    public String getProjects(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        if(!authService.isAdministrator()){
            return "redirect:/";
        }

        List<Project> projects = projectRepo.getList();
        modelMap.addAttribute("projects", projects);

        return "project/list";
    }

    public String getOverview(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        User user = authService.getUser();

        List<Project> projects = projectRepo.getOverview(user.getId());
        modelMap.addAttribute("projects", projects);

        return "project/overview";
    }


    public String addPhone(Long id, ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        String permission = getPermission(Long.toString(id));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/unauthorized";
        }

        Project project = projectRepo.get(id);
        modelMap.put("project", project);
        return "project/phone";
    }

}
