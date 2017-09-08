package com.belintersat.bot.Parser.Lists;

import java.util.HashMap;

/**
 * Created by KrylovichVI on 20.06.2017.
 */
public class BelintersatList{
    private HashMap<String, String> belintersatMap;

    public BelintersatList(){
        belintersatMap = new HashMap();

    }

    public HashMap<String, String> getBelintersatMap() {
        return belintersatMap;
    }

    public void addMap(String key, String str){
        belintersatMap.put(key,str);
    }
}
