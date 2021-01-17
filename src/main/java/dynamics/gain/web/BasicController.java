package dynamics.gain.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import dynamics.gain.service.BasicService;


@Controller
public class BasicController {

	private static final Logger log = Logger.getLogger(BasicController.class);

	@Autowired
	BasicService basicService;

	@RequestMapping(value="/", method=RequestMethod.GET)
	public String index(){
		return basicService.index();
	}

	@RequestMapping(value="/home", method=RequestMethod.GET)
	public String home(){
		return "index";
	}

	@RequestMapping(value="/pricing", method=RequestMethod.GET)
	public String pricing(){
		return "basic/pricing";
	}

	@RequestMapping(value="/signin", method=RequestMethod.GET)
	public String signin(){
		return basicService.showSignin();
	}

	@RequestMapping(value="/privacy", method=RequestMethod.GET)
	public String privacy(){
		return "basic/privacy";
	}

	@RequestMapping(value="/issues/report", method=RequestMethod.GET)
	public String beginReport(){
		return basicService.beginReport();
	}

	@RequestMapping(value="/issues/report", method=RequestMethod.POST)
	public String reportIssue(ModelMap modelMap,
							  RedirectAttributes redirect,
							  @RequestParam(value="email", required = true ) String email,
							  @RequestParam(value="issue", required = true ) String issue){
		return basicService.reportIssue(email, issue, modelMap, redirect);
	}

	@RequestMapping(value="/unauthorized", method=RequestMethod.GET)
	public String unauthorized(){
		return "401";
	}

}