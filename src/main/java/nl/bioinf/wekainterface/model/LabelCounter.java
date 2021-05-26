package nl.bioinf.wekainterface.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.w3c.dom.Attr;
import weka.core.AttributeStats;
import weka.core.Instance;
import weka.core.Instances;
import weka.experiment.Stats;

import javax.swing.text.NumberFormatter;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
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
        this.data = reader.readArff(file);
    }

    /**
     * Creates a Map<String, Map<String, Map<String, Integer>>> structure. First map holds each class label as key with
     * a second Map as value. This Map holds each attribute as key and a third Map as its value. This third Map holds
     * the labels for each attribute as its key and the occurrence of those labels as its value. The occurrence is set
     * at 0.
     */
    public void setGroups(){
        for (int attributeIndex=0;attributeIndex<this.data.classAttribute().numValues();attributeIndex++) { // iterate over each class label
            // Setting the class label as the key for the first Map, in the case of weather.nominal = {yes,no}
            String classLabel = this.data.classAttribute().value(attributeIndex);
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
        int numAttributes = this.data.numAttributes();
        for (int attributeIndex = 0; attributeIndex < numAttributes; attributeIndex++){ // iterating over each attribute name

            // setting attribute name and adding it to the label list
            String attribute = this.data.attribute(attributeIndex).name();

            // attribute array to later count the occurrence of each label for each attribute
            if (!attributeArray.contains(attribute)){
                attributeArray.add(attribute);
            }
            boolean isNominal = this.data.attribute(attributeIndex).isNominal();
            boolean isNumeric = this.data.attribute(attributeIndex).isNumeric();
            // Setting labels when the attribute isn't the class attribute I.E. the attribute that is being classified
            if (attributeIndex != this.data.classIndex()){
                if (isNominal){
                    setLabelsNominal(attributeIndex, attribute, attributes);
                }
                else if (isNumeric){
                    setLabelsNumeric(attributeIndex, attribute, attributes);
                }
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
    private void setLabelsNominal(int attributeIndex, String attribute, AttributeMap attributeMap){
        // bottom level map that holds the label as key and it's occurrence count as value
        LabelMap labelMap = new LabelMap();
        int numValues = this.data.attribute(attributeIndex).numValues();
        for (int valueIndex = 0;valueIndex < numValues; valueIndex++){ // iterate over each label
            String label = this.data.attribute(attributeIndex).value(valueIndex);
            labelMap.addLabel(label);
        }
        attributeMap.addAttribute(attribute, labelMap);
    }

    private void setLabelsNumeric(int attributeIndex, String attribute, AttributeMap attributeMap){
        LabelMap labelMap = new LabelMap();
        Stats stats = this.data.attributeStats(attributeIndex).numericStats;
        double numGroups = Math.round((stats.max - stats.min) / stats.stdDev);

        int numDecimals = numDecimals(stats.min);
        System.out.println((stats.max - stats.min) / numGroups);
        double groupInterval = roundTo((stats.max - stats.min) / numGroups, numDecimals);
        double intervalStart = stats.min;

//        System.out.println(this.data.attribute(attributeIndex));
        for (int groupIndex = 0; groupIndex < numGroups; groupIndex++){
            String labelGroup;
            double intervalEnd = roundTo((intervalStart + groupInterval), numDecimals);
            if (groupIndex == numGroups-1){
                labelGroup = intervalStart + "-" + stats.max;
//                System.out.println("Last group");
            }else {
                labelGroup = intervalStart + "-" + intervalEnd;
//                System.out.println("Not last group");
            }
//            System.out.println("Start value = " + intervalStart);
//            System.out.println("Group Interval = " + groupInterval);
//            System.out.println("End value = " + intervalEnd);
//            System.out.println("Interval = " + labelGroup + "\n");
            labelMap.addLabel(labelGroup);
            intervalStart = roundTo(groupInterval + intervalStart, numDecimals);
        }
        attributeMap.addAttribute(attribute, labelMap);
    };

    /**
     * For each label in the instance, increase it's occurrence count by 1, depending on which class label it has.
     */
    public void countLabels(){
        for (int instanceIndex = 0; instanceIndex < data.numInstances(); instanceIndex++){
            Instance instance = data.instance(instanceIndex);
            String[] values = instance.toString().split(",");
            for (int valueIndex = 0; valueIndex < instance.numValues(); valueIndex++){
                AttributeMap attributeMap = groups.get(values[instance.classIndex()]);
                if(valueIndex != instance.classIndex()){
                    String attribute = attributeArray.get(valueIndex);
                    LabelMap labelMap = attributeMap.getLabelMap(attribute);
                    try{
                        double value = Double.parseDouble(values[valueIndex]);
                        countNumeric(value, labelMap);
                    }catch (NumberFormatException e){
                        countNominal(values[valueIndex], labelMap);
                    }
                }
            }
        }
    }

    private void countNumeric(double value, LabelMap labelMap){
        for (Map.Entry<String, Integer> entry: labelMap.getLabelMap().entrySet()){
            String[] interval = entry.getKey().split("-");
//            System.out.println("Interval = " + Arrays.toString(interval));
            if (value >= Double.parseDouble(interval[0]) && value < Double.parseDouble(interval[1])){
                labelMap.incrementLabel(entry.getKey());
//                System.out.println("Value " + value + " added to labelMap " + labelMap.getLabelMap().toString());
                break;
            }
        }
    }

    private void countNominal(String label, LabelMap labelMap){
        labelMap.incrementLabel(label); // adding a count
    }

    private double roundTo(double value, int numDecimals){
        DecimalFormatSymbols dfSymbols = new DecimalFormatSymbols();
        dfSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#." + "#".repeat(Math.max(0, numDecimals)), dfSymbols);
        String decimals = Double.toString(value).split("\\.")[1];

        if(numDecimals < decimals.length()){

            int lastDigit = Character.getNumericValue(decimals.toCharArray()[numDecimals]);
            if (lastDigit >= 5){
                df.setRoundingMode(RoundingMode.UP);
            }else {
                df.setRoundingMode(RoundingMode.DOWN);
            }
            System.out.println("Formatted value = " + df.format(value));
            return Double.parseDouble(df.format(value));
        }
        return value;
    }

    private int numDecimals(double d){
        String text = Double.toString(Math.abs(d));
        int integerPlaces = text.indexOf('.');
        return text.length() - integerPlaces - 1;
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
        String file = "C:/Program Files/Weka-3-8-4/data/iris.arff";
        LabelCounter labelCounter = new LabelCounter();
        labelCounter.readData(new File(file));
//        System.out.println(labelCounter.roundTo((5.255), 2));
        labelCounter.setGroups();
        labelCounter.countLabels();
        System.out.println(labelCounter.mapToJSON());
    }
}
