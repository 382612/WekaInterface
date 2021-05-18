package nl.bioinf.wekainterface.webcontrol;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class InformationController {


    @RequestMapping(value="information")
    public String getInfoPage(Model model){
        model.addAttribute("name", "WekaAlgorithms");
        return "infAlgorithms";
    }

    @GetMapping(value = "/j48")
    public String getInfoPageJ48(Model model){
        model.addAttribute("name", "J48");
        return "infAlgorithms";
    }

    @GetMapping(value = "/NayvesBayes")

    public String getInfoPageNayvesBayes(Model model){
        model.addAttribute("name", "NayvesBayes");
        return "infAlgorithms";
    }
    @GetMapping(value = "/IBK")
    public String getInfoPageIBK(Model model){
        model.addAttribute("name", "IBK");

        return "infAlgorithms";
    }

    @GetMapping(value = "/OneR")
    public String getInfoPageOneR(Model model){
        model.addAttribute("name", "OneR");

        return "infAlgorithms";
    }
    @GetMapping(value = "/ZeroR")
    public String getInfoPageZeroR(Model model){
        model.addAttribute("name", "ZeroR");

        return "infAlgorithms";
    }
}
