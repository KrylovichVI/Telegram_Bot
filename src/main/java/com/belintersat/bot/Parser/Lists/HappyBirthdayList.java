package com.belintersat.bot.Parser.Lists;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by KrylovichVI on 23.06.2017.
 */
public class HappyBirthdayList {
    private ArrayList<String> happyBirthdayMap;

    public HappyBirthdayList(){
        happyBirthdayMap = new ArrayList<String>();
    }

    public ArrayList<String> getHappyBirthdayMap() {
        return happyBirthdayMap;
    }

    public void addUser(String name){
        happyBirthdayMap.add(name);
    }

    public ArrayList<String> getUsers(){
        ArrayList<String> result = new ArrayList();
        for(String data: happyBirthdayMap){
            String str = data.substring(0, 6);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.");
            if(dateFormat.format(new Date()).equals(str)){
                result.add(data.substring(11, data.length()));
            }
        }
        return result;
    }
}
