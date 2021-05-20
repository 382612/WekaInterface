package nl.bioinf.wekainterface.service;

import nl.bioinf.wekainterface.model.DataReader;
import nl.bioinf.wekainterface.model.WekaClassifier;
import org.springframework.stereotype.Service;
import weka.core.Instances;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Service
public class ClassificationService {
    public List<String> classify(String arffFileName, String classifierName){
        try {
            DataReader reader = new DataReader();
            File arffFile = new File(arffFileName);
            Instances instances = reader.readArff(arffFile);
            WekaClassifier wekaClassifier = new WekaClassifier();
            String result = wekaClassifier.test(instances, classifierName);
            String[] resultElements = result.split("\n");
            return Arrays.asList(resultElements);
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("something went wrong " + e.getMessage());
        }
    }
}
