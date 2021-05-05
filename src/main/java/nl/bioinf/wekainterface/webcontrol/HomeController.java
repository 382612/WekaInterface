package nl.bioinf.wekainterface.webcontrol;

import nl.bioinf.wekainterface.model.DataReader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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

    @GetMapping(value = "/explorer")
    public String getWekaExplorerPage(Model model){
        DataReader reader = new DataReader();
        List<String> filenames = reader.getDataSetNames();
        model.addAttribute("filenames", filenames);
        return "wekaExplorerPage";
    }

}
