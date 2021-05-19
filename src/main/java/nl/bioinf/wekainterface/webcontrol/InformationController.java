package nl.bioinf.wekainterface.webcontrol;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class InformationController {


    @RequestMapping(value="information")
    public String getInfoPage(Model model){
        String[] AlgoList = {"ZeroR", "OneR","NaiveBayes","IBK","J48"};
        model.addAttribute("Algonames", AlgoList);
        return "infAlgorithms";
    }


}
