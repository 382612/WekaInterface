package nl.bioinf.wekainterface.webcontrol;

import nl.bioinf.wekainterface.model.DataReader;
import nl.bioinf.wekainterface.model.LabelCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class ExplorerController {

    @Autowired
    private DataReader dataReader;

    @GetMapping(value = "/explorer")
    public String getWekaExplorerPage(Model model){
        List<String> filenames = dataReader.getDataSetNames();
        model.addAttribute("filenames", filenames);
        return "wekaExplorerPage";
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
