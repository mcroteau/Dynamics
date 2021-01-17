package dynamics.gain.service;

import com.google.gson.Gson;
import dynamics.gain.common.Http;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import dynamics.gain.common.Constants;
import dynamics.gain.common.Utils;
import dynamics.gain.model.*;
import dynamics.gain.repository.*;
import xyz.strongperched.Parakeet;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.*;

@Service
public class UserService {

    private static final Logger log = Logger.getLogger(UserService.class);

    Gson gson = new Gson();

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AuthService authService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ReCaptchaService reCaptchaService;

    @Autowired
    ProjectService projectService;

    @Autowired
    private Environment env;

    private String getPermission(String id){
        return Constants.ACCOUNT_MAINTENANCE + id;
    }


    public String getUsers(ModelMap modelMap){
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        if(!authService.isAdministrator()){
            return Constants.UNAUTHORIZED_REDIRECT;
        }

        List<User> people = userRepo.findAll();
        modelMap.addAttribute("users", people);

        return "user/index";
    }

    public String getEditUser(Long id, ModelMap modelMap){
        String permission = getPermission(Long.toString(id));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/";
        }

        User user = userRepo.get(id);
        modelMap.addAttribute("user", user);

        return "user/edit";
    }



    public String editPassword(Long id, ModelMap modelMap) {

        String permission = getPermission(Long.toString(id));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/";
        }

        User user = userRepo.get(id);
        modelMap.addAttribute("user", user);
        return "user/edit_password";
    }


    public String updatePassword(User user, ModelMap modelMap, RedirectAttributes redirect) {

        String permission = getPermission(Long.toString(user.getId()));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/";
        }

        if(user.getPassword().length() < 7){
            redirect.addFlashAttribute("error", "Passwords must be at least 7 characters long.");
            return "redirect:/signup";
        }

        if(!user.getPassword().equals("")){
            user.setPassword(xyz.strongperched.Parakeet.dirty(user.getPassword()));
            userRepo.updatePassword(user);
        }

        redirect.addFlashAttribute("message", "password successfully updated");
        return "redirect:/signout";

    }

    public String deleteUser(Long id, ModelMap modelMap, RedirectAttributes redirect) {
        if(!authService.isAdministrator()){
            redirect.addFlashAttribute("message", "You don't have permission");
            return Constants.UNAUTHORIZED_REDIRECT;
        }

        User user = userRepo.get(id);

        redirect.addFlashAttribute("message", "Successfully deleted user");

        return "redirect:/admin/users";
    }

    public String disableUser(Long id, ModelMap modelMap, RedirectAttributes redirect) {
        if(!authService.isAdministrator()){
            redirect.addFlashAttribute("error", "You don't have permission");
            return "redirect:/admin/users";
        }

        User user = userRepo.get(id);
        user.setDisabled(true);
        user.setDateDisabled(Utils.getDate());

        redirect.addFlashAttribute("message", "Successfully disabled user");

        return "redirect:/admin/users";
    }


    public String signup(String uri, ModelMap modelMap) {
        authService.signout();
        return "user/signup";
    }


    public String register(String reCaptchaResponse, User user, HttpServletRequest req, RedirectAttributes redirect) {

        if(user == null){
            redirect.addFlashAttribute("message", "a error on our end, please give it another go.");
            return "redirect:/signup";
        }

        if(!reCaptchaService.validates(reCaptchaResponse) &&
                !Utils.isTestEnvironment(env)){
            redirect.addFlashAttribute("message", "Did you forget to check the box thing?");
            return "redirect:/signup";
        }

        if(!Utils.isValidMailbox(user.getUsername())){
            redirect.addFlashAttribute("message", "Username must be a valid email.");
            return "redirect:/signup";
        }

        User existingUser = userRepo.getByUsername(user.getUsername());
        if(existingUser != null){
            redirect.addFlashAttribute("message", "User exists with same username.");
            return "redirect:/signup";
        }

        if(user.getPassword().equals("")) {
            redirect.addFlashAttribute("message", "Password cannot be blank");
            return "redirect:/signup";
        }

        if(user.getPassword().length() < 7){
            redirect.addFlashAttribute("message", "Password must be at least 7 characters long.");
            return "redirect:/signup";
        }

        String password = user.getPassword();
        String passwordHashed = Parakeet.dirty(user.getPassword());

        try{

            user.setPassword(passwordHashed);
            userRepo.save(user);

            User savedUser = userRepo.getByUsername(user.getUsername());
            Role defaultRole = roleRepo.find(Constants.ROLE_ACCOUNT);

            userRepo.saveUserRole(savedUser.getId(), defaultRole.getId());
            String permission = Constants.ACCOUNT_MAINTENANCE + savedUser.getId();
            userRepo.savePermission(savedUser.getId(), permission);


            String body = "<h1>Okay!</h1>"+
                    "<p>Thank you for registering! We are on it, Okay!</p>";

            if(!Utils.isTestEnvironment(env)) {
                emailService.send(savedUser.getUsername(), "Successfully Registered", body);
                phoneService.support("Okay " + user.getUsername());
            }

        }catch(Exception e){
            e.printStackTrace();
            log.info(e.getMessage());
            redirect.addFlashAttribute("message", "Will you contact us? Email us with the subject, support@amadeus.social. Our programmers missed something. Gracias!");
            return("redirect:/signup");
        }

        log.info(userRepo.getCount() + " " + authService + " " + user.getUsername());
        if(!authService.signin(user.getUsername(), password)) {
            redirect.addFlashAttribute("message", "Thank you for registering. We hope you enjoy!");
            return "redirect:/";
        }

        req.getSession().setAttribute("username", user.getUsername());

        return "redirect:/";
    }

    public String homeSignup(SignMeUp signMeUp, HttpServletRequest req, RedirectAttributes redirect) {

        signMeUp.setUri(Http.convert(signMeUp.getUri()));

        if(signMeUp == null){
            redirect.addFlashAttribute("message", "a error on our end, please give it another go.");
            return "redirect:/home";
        }

        if(!Utils.isValidMailbox(signMeUp.getUsername())){
            redirect.addFlashAttribute("message", "Email must be a valid. Please try again!");
            return "redirect:/home";
        }

        User existingUser = userRepo.getByUsername(signMeUp.getUsername());
        if(existingUser != null){
            redirect.addFlashAttribute("message", "It looks like you already have an account with us. Try signing in.");
            return "redirect:/signin";
        }

        User user = new User();
        user.setUsername(signMeUp.getUsername());

        String plainPassword = signMeUp.getPassword();
        user.setPassword(Parakeet.dirty(plainPassword));

        boolean isRandom = false;
        if(signMeUp.getPassword().equals("")){
            isRandom = true;
            plainPassword = "password-" + Utils.getRandomString(3);
            String randPassword = Parakeet.dirty(plainPassword);
            user.setPassword(randPassword);
        }
        User savedUser = userRepo.save(user);

        Project project = new Project();
        project.setUri(signMeUp.getUri());
        project.setName("Project One");
        project.setUserId(savedUser.getId());

        Project savedProject = projectRepo.save(project);
        String permission = projectService.getPermission(Long.toString(savedProject.getId()));
        userRepo.savePermission(savedUser.getId(), permission);

        if(authService.signin(user.getUsername(), plainPassword)){
            String site = "http://localhost:8080/o/signin";
            String subject = "You're all set!";

            StringBuffer message = new StringBuffer();
            message.append("<h1>Okay! Project Monitored!</h1>" +
                    "<p>You're all set, your project @ " + signMeUp.getUri() +
                    " is all configured. You can now sign in and enter a good phone number " +
                    "where we can reach you if your site goes down.</p>");
            if(isRandom) {
                message.append("<p>Your password : <strong>" + plainPassword + "</strong></p>");
            }
            message.append("<p><a href=\"" + site + "\">Signin!</a></p>");

            emailService.send(user.getUsername(), "support@okay.page", subject, message.toString());

            req.getSession().setAttribute("username", user.getUsername());

            redirect.addFlashAttribute("message", "You're all set! Project configured!");
            return "redirect:/project/overview";
        }else{
            redirect.addFlashAttribute("message", "Please signin to continue");
            return "redirect:/";
        }

    }

    public String sendReset(String username, RedirectAttributes redirect, HttpServletRequest req) {

        try {

            User user = userRepo.getByUsername(username);
            if (user == null) {
                redirect.addFlashAttribute("message", "Unable to find user.");
                return ("redirect:/user/reset");
            }

            String resetUuid = Utils.getRandomString(13);
            user.setUuid(resetUuid);
            userRepo.updateUuid(user);

            StringBuffer url = req.getRequestURL();

            String[] split = url.toString().split("/o/");
            String httpSection = split[0];

            String resetUrl = httpSection + "/o/user/confirm_reset?";
            String params = "username=" + URLEncoder.encode(user.getUsername(), "utf-8") + "&uuid=" + resetUuid;
            resetUrl += params;

            String body = "<h1>Okay</h1>" +
                    "<p>Password Reset ~ " +
                    "<a href=\"" + resetUrl + "\">" + resetUrl + "</a></p>";

            emailService.send(user.getUsername(), "Reset Password", body);

        }catch(Exception e){
            e.printStackTrace();
        }

        return "user/send_reset";
    }

    public String resetView(String uuid, String username, ModelMap modelMap,RedirectAttributes redirect) {

        User user = userRepo.getByUsernameAndUuid(username, uuid);
        if (user == null) {
            redirect.addFlashAttribute("error", "Unable to locate user.");
            return "redirect:/user/reset";
        }

        modelMap.addAttribute("user", user);
        return "user/confirm";
    }

    public String resetPassword(User user, ModelMap modelMap, RedirectAttributes redirect) {

        if(user.getPassword().length() < 7){
            redirect.addFlashAttribute("user", user);
            redirect.addFlashAttribute("message", "Passwords must be at least 7 characters long.");
            return "redirect:/user/confirm?username=" + user.getUsername() + "&uuid=" + user.getUuid();
        }

        if(!user.getPassword().equals("")){
            String password = xyz.strongperched.Parakeet.dirty(user.getPassword());
            user.setPassword(password);
            userRepo.updatePassword(user);
        }

        redirect.addFlashAttribute("message", "Password successfully updated");
        return "user/success";
    }


    public String suspendUser(String id, ModelMap modelMap, RedirectAttributes redirect) {

        if(!authService.isAdministrator()){
            redirect.addFlashAttribute("message", "You don't have permission to do this!");
            return "redirect:/user/profile/" + id;
        }

        User user = userRepo.get(Long.parseLong(id));
        user.setDateDisabled(Utils.getDate());
        user.setDisabled(true);
        userRepo.disable(user);

        modelMap.addAttribute("message", "User suspended.");
        return "redirect:/user/edit/" + id;
    }


    public String renewUser(Long id, ModelMap modelMap, RedirectAttributes redirect) {
        if(!authService.isAdministrator()){
            redirect.addFlashAttribute("message", "You don't have permission to do this!");
            return "redirect:/";
        }

        User user = userRepo.get(id);
        userRepo.renew(user);

        modelMap.addAttribute("message", "User renewed.");
        return "redirect:/user/edit/" + id;
    }

}
