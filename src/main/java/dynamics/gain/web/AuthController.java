package dynamics.gain.web;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import dynamics.gain.model.User;
import dynamics.gain.service.AuthService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthController {

	private static final Logger log = Logger.getLogger(AuthController.class);

    Gson gson = new Gson();

	@Autowired
	AuthService authService;

	@RequestMapping(value="/authenticate", method=RequestMethod.POST)
	public String authenticateAccount(ModelMap modelMap,
							   RedirectAttributes redirect,
							   HttpServletRequest request,
							   @ModelAttribute("user") User user){
		return authService.authenticateUser(user, redirect, request);
	}


	@RequestMapping(value="/signout", method=RequestMethod.GET)
	public String unAuthenticateAccount(HttpServletRequest request,
						  RedirectAttributes redirect){
		return authService.deAuthenticateUser(redirect, request);
	}

}