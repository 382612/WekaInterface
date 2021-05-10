package nl.bioinf.wekainterface.model;

import java.util.HashMap;
import java.util.Map;


/**
 @author: Jelle 387615
 */
public class LabelMap {
    private Map<String, Integer> labelMap = new HashMap<>();

    public void addLabel(String label){
        labelMap.put(label, 0);
    }

    public void incrementLabel(String label){
        labelMap.computeIfPresent(label, (key, oldValue) -> oldValue+1);
    }

    public Map<String, Integer> getLabelMap(){
        return labelMap;
    }
}
