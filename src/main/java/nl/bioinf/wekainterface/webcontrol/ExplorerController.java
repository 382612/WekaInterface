package nl.bioinf.wekainterface.webcontrol;

import nl.bioinf.wekainterface.model.DataReader;
import nl.bioinf.wekainterface.model.LabelCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ExplorerController {

    @Autowired
    private DataReader dataReader;
    @Autowired
    private LabelCounter labelCounter;

    @GetMapping(value = "/explorer")
    public String getClassifierFormPage(Model model){
        List<String> filenames = dataReader.getDataSetNames();
        model.addAttribute("filenames", filenames);
        return "classifierForm";
    }

    @PostMapping(value = "/explorer")
    public String postClassifierFormPage(HttpServletRequest request, RedirectAttributes redirect){
        String filename = request.getParameter("file-name");
        redirect.addFlashAttribute("filename", filename);
        return "redirect:/results";
    }

    @GetMapping(value = "/results")
    public String getResultsPage(Model model){
        String filename = (String) model.getAttribute("filename");
        model.addAttribute("filename", filename);
        return "results";
    }

    @GetMapping(value = "/test")
    public String plotWeatherData(Model model) throws IOException {
        String file = "/Users/Marijke/wekafiles/data/weather.nominal.arff";
        labelCounter.readData(file);
        labelCounter.setGroups();
        labelCounter.countLabels();
        model.addAttribute("data", labelCounter.mapToJSON());
        model.addAttribute("attributes", labelCounter.getAttributeArray());
        model.addAttribute("classLabel", labelCounter.getClassLabel());
        return "dataExplorer";
    }
}
