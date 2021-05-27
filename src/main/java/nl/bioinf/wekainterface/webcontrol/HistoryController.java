package nl.bioinf.wekainterface.webcontrol;


import nl.bioinf.wekainterface.model.AlgortihmsInformation;
import nl.bioinf.wekainterface.service.SerializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * @author Bart Engels
 */

@Controller
public class HistoryController {

    @Autowired
    private SerializationService serializationService;

    @PostMapping(value = "/history")
    public String getHistoryPage(Model model){

        model.addAttribute("info" , serializationService);

        return "hisotrydummpypage";
    }
    }
