package dynamics.gain.web;

import com.google.gson.Gson;
import dynamics.gain.model.Donation;
import dynamics.gain.model.DonationInput;
import dynamics.gain.service.DonateService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DonateController {

    private static final Logger log = Logger.getLogger(DonateController.class);

    Gson gson = new Gson();

    @Autowired
    DonateService donateService;

    @GetMapping(value="/donate")
    public String index(){
        return "donate/index";
    }

    @GetMapping(value="/donate/once")
    public String once(){
        return "index";
    }

    @GetMapping(value="/donate/cleanup")
    public String cleanup(){
        return donateService.cleanup();
    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @CrossOrigin(origins="*")
    @RequestMapping(value="/donate/make", method=RequestMethod.POST, consumes="application/json")
    public @ResponseBody String make(@RequestBody DonationInput donationInput, Exception ex){
//        log.info("Returning HTTP 400 Bad Request", ex);
        return gson.toJson(donateService.make(donationInput));
    }
}
