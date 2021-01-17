package dynamics.gain.service;

import dynamics.gain.common.Constants;
import dynamics.gain.common.Http;
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

    private void setProjectAttributes(Project project) {
        try {
            ProjectStatus projectStatus = projectRepo.getProjectStatus(project.getId());

            if(!projectStatus.isInitialSaving()) {
                SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_SEARCH_FORMAT);
                Date date = format.parse(Long.toString(projectStatus.getValidationDate()));

                PrettyTime prettyTime = new PrettyTime();
                project.setPrettyTime(prettyTime.format(date));

                project.setActualDate(Utils.getActualDate(projectStatus.getValidationDate()));
                project.setStatus(Long.toString(projectStatus.getStatusCode()));
                project.setAvgResp(Utils.round(projectStatus.getAvgResponse(), 3));

                double uptime = projectStatus.getOperationalHttpValidations() / projectStatus.getTotalHttpValidations();
                double percentUptime = Utils.round(uptime * 100, 3);
                project.setPercentUptime(percentUptime);
            }

            if(projectStatus.isInitialSaving()){
                project.setInitial(true);
                project.setStatus("getting data...");
            }

        }catch(Exception e){ }

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
        setProjectAttributes(project);

        List<ProjectPhone> phones = projectRepo.getPhones(id);
        project.setProjectPhones(phones);

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
        project.setUri(Http.convert(project.getUri()));

        Project savedProject = projectRepo.save(project);
        String permission = getPermission(Long.toString(savedProject.getId()));
        userRepo.savePermission(authdUser.getId(), permission);

        return "redirect:/project/overview";
    }

    public Status validate(String uri) {
        Status status = new Status();

        uri = Http.convert(uri);

        int statusCode = 500;
        double responseTime = 0.0;
        try {
            Date start = new Date();
            URL url = new URL(uri);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(6701);
            statusCode = connection.getResponseCode();
            connection.disconnect();

            Date end = new Date();
            double diff = end.getTime() - start.getTime();
            responseTime = diff/1000;

        }catch(Exception e){ }

        status.setStatus(statusCode);
        status.setResponseTime(responseTime);
        return status;
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

        projectRepo.deleteProjectStatus(id);
        projectRepo.deleteProjectPhones(id);
        projectRepo.deleteProjectEmails(id);
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
        projects.stream().forEach( project -> setProjectAttributes(project));

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

    public String addPhone(Long id, ProjectPhone projectPhone, HttpServletRequest request, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        String permission = getPermission(Long.toString(id));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/";
        }

        projectPhone.setPhone(projectPhone.getPhone().trim());

        if(projectPhone.getPhone().contains(" ")){
            redirect.addFlashAttribute("message", "Phone cannot contain spaces");
            return "redirect:/project/phone/add/" + id;
        }

        if(Utils.containsSpecialCharacters(projectPhone.getPhone())){
            redirect.addFlashAttribute("message", "Phone cannot contain special characters, only numbers");
            return "redirect:/project/phone/add/" + id;
        }

        if(!phoneService.validate(projectPhone.getPhone())){
            redirect.addFlashAttribute("message", "We were unable to send a message to that number, want to try again?");
            return "redirect:/project/phone/add/" + id;
        }

        redirect.addFlashAttribute("message", "Successfully added phone number");
        projectRepo.addPhone(projectPhone);

        return "redirect:/project/" + id;
    }

    public String deletePhone(Long id, HttpServletRequest request, RedirectAttributes redirect) {
        String permission = getPhonePermission(Long.toString(id));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/";
        }
        projectRepo.deletePhone(id);
        return "redirect:/project/" + id;
    }

    public String addEmail(Long id, ModelMap modelMap) {
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
        return "project/email";
    }

    public String addEmail(Long id, ProjectEmail projectEmail, HttpServletRequest request, RedirectAttributes redirect) {
        String permission = getPermission(Long.toString(id));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/";
        }

        if(!Utils.isValidMailbox(projectEmail.getEmail())){
            redirect.addFlashAttribute("message", "Please enter a valid email");
            return "redirect:/project/email/add/" + id;
        }

        projectRepo.addEmail(projectEmail);
        return "redirect:/project/" + id;
    }

    public String deleteEmail(Long id, HttpServletRequest request, RedirectAttributes redirect) {
        String permission = getEmailPermission(Long.toString(id));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/";
        }

        projectRepo.deleteEmail(id);
        return "redirect:/project/" + id;
    }

}
