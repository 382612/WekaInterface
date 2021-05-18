package nl.bioinf.wekainterface.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import weka.core.Instance;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
@author jelle 387615
 Given a File, counts the occurrence of each label for each attribute and seperates it by classlabel
 */
@Component
public class LabelCounter {

    private Instances data;
    private List<String> attributeArray = new ArrayList<>();
    private Map<String, AttributeMap> groups = new HashMap<>();

    /**
     * Reads arff file and stores Instances in the class
     * @param file arff file
     * @throws IOException if file can't be found
     */
    public void readData(File file) throws IOException {
        DataReader reader = new DataReader();
        data = reader.readArff(file);
    }

    /**
     * Creates a Map<String, Map<String, Map<String, Integer>>> structure. First map holds each class label as key with
     * a second Map as value. This Map holds each attribute as key and a third Map as its value. This third Map holds
     * the labels for each attribute as its key and the occurrence of those labels as its value. The occurrence is set
     * at 0.
     */
    public void setGroups(){
        for (int i=0;i<this.data.classAttribute().numValues();i++) { // iterate over each class label
            // Setting the class label as the key for the first Map, in the case of weather.nominal = {yes,no}
            String classLabel = this.data.classAttribute().value(i);
            // creating the 2nd Map with attribute names as keys and labels as value's
            AttributeMap attributes = setAttributes();

            this.groups.put(classLabel, attributes);
        }
    }

    /**
     * For every attribute in the dataset creates an entry in a Map with the attribute name as the key and a Map as the
     * value for this key. This Map holds each attribute label as its key and the occurrence of this label as its value.
     * @return Map<Attribute name, Map<Attribute label, Label occurrence>>
     */
    private AttributeMap setAttributes(){
        // creating the Map with attribute names as keys and labels as value's
        AttributeMap attributes = new AttributeMap();

        for (int u = 0; u < this.data.numAttributes(); u++){ // iterating over each attribute name

            // setting attribute name and adding it to the label list
            String attribute = this.data.attribute(u).name();

            // attribute array to later count the occurrence of each label for each attribute
            if (!attributeArray.contains(attribute)){
                attributeArray.add(attribute);
            }

            // Setting labels when the attribute isn't the class attribute I.E. the attribute that is being classified
            if (u != this.data.classIndex()){
                setLabels(u, attribute, attributes);
            }
        }
        return attributes;
    }

    /**
     * For the given attribute/attribute index, add each label to the Map and set its value to 0.
     * @param attributeIndex index of the attribute that is currently being added to Map<String, Map<String, Integer>> as the key
     * @param attribute attribute name
     * @param attributeMap Map<Attribute name, Map<Attribute label, Label occurrence>> where attribute labels need to be added
     */
    private void setLabels(int attributeIndex, String attribute, AttributeMap attributeMap){
        // bottom level map that holds the label as key and it's occurrence count as value
        LabelMap labelMap = new LabelMap();
        for (int y=0;y<this.data.attribute(attributeIndex).numValues();y++){ // iterate over each label
            String label = this.data.attribute(attributeIndex).value(y);
            labelMap.addLabel(label); // label is added with an occurrence of 0
        }
        attributeMap.addAttribute(attribute, labelMap);
    }

    /**
     * For each label in the instance, increase it's occurrence count by 1, depending on which class label it has.
     */
    public void countLabels(){
        for (int i = 0; i < data.numInstances(); i++){
            Instance instance = data.instance(i);
            String[] values = instance.toString().split(",");
            for (int u = 0; u < instance.numValues(); u++){ // iterate over the values in the instance
                // attributeMap is the map containing the attribute as the key and the labels + their counts as the
                // Value in the form of a submap
                AttributeMap attributeMap = groups.get(values[instance.classIndex()]); // Get attribute map for the right class label
                if(u != instance.classIndex()){
                    // getting the labels and their count for the attribute
                    String attribute = attributeArray.get(u);
                    LabelMap labelMap = attributeMap.getLabelMap(attribute);

                    String label = values[u]; // The label of the attribute attribute:label windy:TRUE
                    labelMap.incrementLabel(label); // adding a count
                }
            }
        }
    }

    public List<String> getAttributeArray() {
        return attributeArray;
    }

    public String getClassLabel(){
        return data.classAttribute().toString().split(" ")[1];
    }

    /**
     * Converts the class label Map into a JSON format to be used in JavaScript
     * @return Map<Class label, Map<Attribute name, Map<Attribute label, Label occurrence>>> in JSON format
     * @throws JsonProcessingException ...
     */
    public String mapToJSON() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, Map<String, Integer>>> countMap = new HashMap<>();
        groups.forEach((classLabel, attributeMap) ->
                countMap.put(classLabel, attributeMap.getJsonMap()));
        return objectMapper.writeValueAsString(countMap);
    }

    /**
     * Main function for testing class
     * @param args no args
     * @throws IOException if file doesn't exist
     */
    public static void main(String[] args) throws IOException {
        String file = "C:/Program Files/Weka-3-8-4/data/weather.nominal.arff";
        LabelCounter labelCounter = new LabelCounter();
        labelCounter.readData(new File(file));
        labelCounter.setGroups();
        labelCounter.countLabels();
        System.out.println(labelCounter.mapToJSON());
    }
}
