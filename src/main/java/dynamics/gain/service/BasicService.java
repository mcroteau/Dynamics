package dynamics.gain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class BasicService {

    @Autowired
    AuthService authService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private EmailService emailService;

    public String index() {
        if(authService.isAuthenticated()){
            return "redirect:/project/overview";
        }

        return "redirect:/home";
    }

    public String showSignin() {
        if(authService.isAuthenticated()){
            return "redirect:/";
        }
        return "basic/signin";
    }

    public String beginReport() {
        phoneService.support("Okay issue");
        return "basic/report";
    }

    public String reportIssue(String email, String issue, ModelMap modelMap, RedirectAttributes redirect) {

        if (email.equals("")) {
            redirect.addFlashAttribute("error", "Please enter a valid email address");
            return "redirect:/issues/report";
        }

        if (issue.equals("")) {
            redirect.addFlashAttribute("error", "Issue was left black, please tell us what happened.");
            return "redirect:/issues/report";
        }

        StringBuffer sb = new StringBuffer();
        sb.append(email);
        sb.append("<br/>");
        sb.append(issue);
        emailService.send("croteau.mike@gmail.com", "Okay!", sb.toString());

        modelMap.addAttribute("message", "Thank you. Issue has been reported.");
        return "basic/success";
    }

}
