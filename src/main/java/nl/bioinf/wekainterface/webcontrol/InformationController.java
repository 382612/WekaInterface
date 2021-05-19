package nl.bioinf.wekainterface.webcontrol;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Controller

public class InformationController {

    @Value("#{'${youtube.link.j48}'.split(',')}")
    private List<String> youtubeLinkJ47;

    @RequestMapping(value="information")
    public String getInfoPage(Model model){
        String[] AlgoList = {"ZeroR", "OneR","NaiveBayes","IBK","J48"};
        model.addAttribute("algonames", AlgoList);
        model.addAttribute("youtubelink", youtubeLinkJ47);
        return "infAlgorithms";
    }


}
