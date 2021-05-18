package nl.bioinf.wekainterface.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class reads data and converts a csv file to an arff file
 * @author Jelle, Marijke
 */

@Component
public class DataReader implements Reader{
    @Value("${example.data.path}")
    private String exampleFilesFolder;
    @Value("${temp.data.path}")
    private String tempFolder;

    /**
     * This method reads an arff file and returns the instances.
     * @param file name of the arff file
     * @return Dataset instances
     * @throws IOException
     */
    @Override
    public Instances readArff(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        ArffLoader.ArffReader arffReader = new ArffLoader.ArffReader(reader);
        Instances data = arffReader.getData();
        data.setClassIndex(data.numAttributes() - 1);
        return data;
    }

    /**
     * This method converts and csv file to an arff file.
     * @param fileName name of the csv file
     * @throws IOException
     */
    @Override
    public void CsvToArff(String fileName) throws IOException {
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(fileName));
        Instances data = loader.getDataSet();
    }

    /**
     * This method creates a list of the demo data names.
     * @return List of demo filenames
     */
    @Override
    public List<String> getDataSetNames() {
        File folder = new File(exampleFilesFolder);
        File[] listOfFiles = folder.listFiles();
        List<String> fileNames = new ArrayList<>();
        for (File file: listOfFiles){
            fileNames.add(file.getName());
        }
        return fileNames;
    }

    /**
     * This method safes an arff file to a temporarily folder.
     * @param file Arff file
     * @throws IOException
     */
    @Override
    public String saveArff(File file) throws IOException {
        File tempFile = File.createTempFile("temp-", ".arff", new File("C:/Users/jelle/Desktop/School/Thema11/Practicum/wekaTempFiles"));
        Instances instances = readArff(file);
        ArffSaver saver = new ArffSaver();
        saver.setInstances(instances);
        saver.setFile(tempFile);
        saver.writeBatch();
        return tempFile.toString();
    }

    /**
     * Not implemented yet
     * @param instances
     * @throws IOException
     */
    @Override
    public void saveArff(Instances instances) throws IOException {
        ArffSaver saver = new ArffSaver();
        saver.setFile(new File(tempFolder));
        saver.writeBatch();
    }

    public static void main(String[] args) throws IOException {
        DataReader dataReader = new DataReader();
        //Instead of using a hardcoded path use the application.properties variable tempFolder
        System.out.println(dataReader.saveArff(new File("C:/Program Files/Weka-3-8-4/data/weather.nominal.arff")));
    }
}
