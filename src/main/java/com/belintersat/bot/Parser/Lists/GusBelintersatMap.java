package com.belintersat.bot.Parser.Lists;


import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GusBelintersatMap {
    private LinkedHashMap<String, ArrayList<String>> linkedMap;

    public GusBelintersatMap() {
        this.linkedMap = new LinkedHashMap<>();
    }

    public LinkedHashMap<String, ArrayList<String>> getLinkedMap() {
        return linkedMap;
    }

    public void addMap(String key, ArrayList<String> value){
        linkedMap.put(key, value);
    }
}
