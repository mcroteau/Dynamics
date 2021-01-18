package dynamics.gain.web;

import dynamics.gain.service.DonateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DonateController {

    @Autowired
    DonateService donateService;

    @GetMapping(value="/donate")
    public String index(){
        return "index.d";
    }

    @GetMapping(value="/donate/once")
    public String once(){
        return "index";
    }

    @GetMapping(value="/donate/monthly")
    public String monthly(){
        return "monthly.d";
    }
}
