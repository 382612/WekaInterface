package nl.bioinf.wekainterface.webcontrol;

import nl.bioinf.wekainterface.model.DataReader;
import nl.bioinf.wekainterface.model.LabelCounter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import weka.core.Instances;

import java.io.IOException;
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

    @GetMapping(value = "/information")
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

    @GetMapping(value = "/upload")
    public String getFileUploadPage(Model model) throws IOException {
        DataReader reader = new DataReader();
        String file = "/Users/Marijke/wekafiles/data/weather.nominal.arff";
        Instances data = reader.readArff(file);
        System.out.println(data.get(0));
        model.addAttribute("data", data.toString());
        return "file-upload";
    }

    @GetMapping(value = "/test")
    public String plotWeatherData(Model model) throws IOException {
        String file = "/Users/Marijke/wekafiles/data/weather.nominal.arff";
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