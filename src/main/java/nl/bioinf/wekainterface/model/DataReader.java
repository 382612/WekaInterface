package nl.bioinf.wekainterface.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataReader implements Reader{
    @Value("${example.data.path}")
    private String exampleFilesFolder;
    @Value("${temp.data.path}")
    private String tempFolder;

    @Override
    public Instances readArff(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        ArffLoader.ArffReader arffReader = new ArffLoader.ArffReader(reader);
        Instances data = arffReader.getData();
        data.setClassIndex(data.numAttributes() - 1);
        return data;
    }

    @Override
    public void CsvToArff(String fileName) throws IOException {
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(fileName));
        Instances data = loader.getDataSet();
    }

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

    @Override
    public void saveArff(File file) throws IOException {
        ArffSaver saver = new ArffSaver();
        saver.setFile(new File(tempFolder));
        saver.setDestination(new File(tempFolder));
    }

    @Override
    public void saveArff(Instances instances) throws IOException {
        ArffSaver saver = new ArffSaver();
        saver.setFile(new File(tempFolder));
        saver.writeBatch();


    }
}
