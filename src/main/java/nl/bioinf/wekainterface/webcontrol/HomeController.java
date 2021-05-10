package nl.bioinf.wekainterface.webcontrol;

import nl.bioinf.wekainterface.model.AlgortihmsInformation;
import nl.bioinf.wekainterface.model.DataReader;
import nl.bioinf.wekainterface.model.LabelCounter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import weka.core.Instances;

import java.io.IOException;

/**
 * @author Marijke Eggink, Jelle Becirspahic & Bart Engels
 */

@Controller

public class HomeController {
    @GetMapping(value = "/home")
    public String getLandingPage(){
        return "landingpage";
    }


    @RequestMapping(value="information")
    public String getInfoPage(){
            return "infoPage";
        }

        @GetMapping(value = "/j48")
        public String getInfoPageJ48(Model model){
            AlgortihmsInformation algortihmsInformation = new AlgortihmsInformation("J48", "Infromation");
            model.addAttribute("name", algortihmsInformation.getName());
            model.addAttribute("information", algortihmsInformation.getInformation());
        return "infAlgorithms";
        }

    @GetMapping(value = "/NayvesBayes")
    public String getInfoPageNayvesBayes(Model model){
        AlgortihmsInformation algortihmsInformation = new AlgortihmsInformation("NayvesBayes", "Infromation");
        model.addAttribute("name", algortihmsInformation.getName());
        model.addAttribute("information", algortihmsInformation.getInformation());
        return "infAlgorithms";
    }

    @GetMapping(value = "/IBK")
    public String getInfoPageIBK(Model model){
        AlgortihmsInformation algortihmsInformation = new AlgortihmsInformation("IBK", "Infromation");
        model.addAttribute("name", algortihmsInformation.getName());
        model.addAttribute("information", algortihmsInformation.getInformation());
        return "infAlgorithms";
    }

    @GetMapping(value = "/OneR")
    public String getInfoPageOneR(Model model){
        AlgortihmsInformation algortihmsInformation = new AlgortihmsInformation("OneR", "Infromation");
        model.addAttribute("name", algortihmsInformation.getName());
        model.addAttribute("information", algortihmsInformation.getInformation());
        return "infAlgorithms";
    }
    @GetMapping(value = "/ZeroR")
    public String getInfoPageZeroR(Model model){
        AlgortihmsInformation algortihmsInformation = new AlgortihmsInformation("ZeroR", "Infromation");
        model.addAttribute("name", algortihmsInformation.getName());
        model.addAttribute("information", algortihmsInformation.getInformation());
        return "infAlgorithms";
    }


    @GetMapping(value = "/upload")
    public String getFileUploadPage(Model model) throws IOException {
        DataReader reader = new DataReader();
        String file = "C:/Program Files/Weka-3-8-4/data/weather.nominal.arff";
        Instances data = reader.readArff(file);
        System.out.println(data.get(0));
        model.addAttribute("data", data.toString());
        return "file-upload";
    }

    @GetMapping(value = "/test")
    public String plotWeatherData(Model model) throws IOException {
        String file = "C:/Program Files/Weka-3-8-4/data/weather.nominal.arff";
        LabelCounter labelCounter = new LabelCounter();
        labelCounter.readData(file);
        labelCounter.setGroups();
        labelCounter.countLabels();
        model.addAttribute("data", labelCounter.mapToJSON());
        model.addAttribute("attributes", labelCounter.getAttributeArray());
        model.addAttribute("classLabel", labelCounter.getClassLabel());
        return "dataExplorer";
    }

}
