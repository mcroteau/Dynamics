package dynamics.gain.service;

import com.google.gson.Gson;
import dynamics.gain.common.Dynamics;
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
    LocationService locationService;

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
                !Dynamics.isTestEnv(env)){
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

            if(!Dynamics.isTestEnv(env)) {
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

}
