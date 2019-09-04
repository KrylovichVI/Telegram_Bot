package com.belintersat.bot.Parser.Lists;


import java.util.ArrayList;
import java.util.HashMap;

public class GusBelintersatMap {
    private HashMap<String, ArrayList<String>> list;

    public GusBelintersatMap() {
        this.list = new HashMap<>();
    }

    public HashMap<String, ArrayList<String>> getList() {
        return list;
    }

    public void addMap(String key, ArrayList<String> value){
        list.put(key, value);
    }
}
