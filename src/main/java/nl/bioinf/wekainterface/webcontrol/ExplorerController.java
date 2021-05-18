package nl.bioinf.wekainterface.webcontrol;

import nl.bioinf.wekainterface.model.DataReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
