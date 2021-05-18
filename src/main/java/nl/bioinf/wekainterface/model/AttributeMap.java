package nl.bioinf.wekainterface.model;

import java.util.HashMap;
import java.util.Map;

/**
 @author: Jelle 387615
 */
public class AttributeMap {
    private Map<String, LabelMap> attributeMap = new HashMap<>();

    public void addAttribute(String attribute, LabelMap labelMap){
        attributeMap.put(attribute, labelMap);
    }

    public LabelMap getLabelMap(String attribute){
        return attributeMap.get(attribute);
    }

    public Map<String, LabelMap> getAttributeMap() {
        return attributeMap;
    }

    public Map<String, Map<String, Integer>> getJsonMap(){
        Map<String, Map<String, Integer>> JsonMap = new HashMap<>();
        attributeMap.forEach((attribute, labelMap) ->
                JsonMap.put(attribute, labelMap.getLabelMap()));
        return JsonMap;
    }
}
