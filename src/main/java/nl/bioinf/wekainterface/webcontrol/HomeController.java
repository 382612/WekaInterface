package nl.bioinf.wekainterface.webcontrol;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Marijke Eggink
 */

@Controller
public class HomeController {
    @GetMapping(value = "/home")
    public String getLandingPage(){
        return "landingpage";
    }

    @GetMapping(value = "/Information")
    public String getInfoPage(){
        return "infoPage";
    }

}
