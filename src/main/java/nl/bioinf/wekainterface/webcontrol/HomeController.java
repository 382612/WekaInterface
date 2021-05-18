package nl.bioinf.wekainterface.webcontrol;

import nl.bioinf.wekainterface.model.AlgortihmsInformation;
import nl.bioinf.wekainterface.model.DataReader;

import nl.bioinf.wekainterface.model.LabelCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import weka.core.Instances;


import java.io.IOException;
import java.util.List;


/**
 * @author Marijke Eggink, Jelle Becirspahic & Bart Engels
  */


@Controller

public class HomeController {

    @GetMapping(value = "/home")
    public String getLandingPage(){
        return "landingpage";
    }

    @GetMapping(value = "/about")
    public String getFileUploadPage(){
        return "about";
    }

    @GetMapping(value = "/contact")
    public String getContactPage(){
        return "contact";
    }

}