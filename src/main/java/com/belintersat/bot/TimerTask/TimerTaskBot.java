package com.belintersat.bot.TimerTask;
import com.belintersat.bot.Bot.Bot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

public class TimerTaskBot extends TimerTask {
    private Bot bot;

    private final int TIME_BIRTH_INT = 830;
    private final int TIME_WEEKEND_INT = 830;
    private final int TIME_WETH_INT = 700;
    private final int TIME_NOTIF_INT = 800;

    private String TIME_EQUALS  = "08:30";
    private String TIME_NOTIFICATION  = "08:00";
    private String TIME_WEATHER  = "07:00";
    private String TIME_WEEKEND  = "08:30";



    private boolean FLAG_BIRTHDAY = false;
    private boolean FLAG_WEATHER = false;
    private boolean FLAG_NOTIFICATION = false;


    public TimerTaskBot(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        Date nowTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");
        SimpleDateFormat dateFormatSlach = new SimpleDateFormat("HH:mm");

        System.out.println(dateFormatSlach.format(nowTime));

        if(nowTime.getDay() < 6) {
            if (dateFormatSlach.format(nowTime).equals(TIME_EQUALS) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_BIRTH_INT) && FLAG_BIRTHDAY == false)) {
                bot.sendMsgBirthday(nowTime);
                FLAG_BIRTHDAY = true;
            }
            if (dateFormatSlach.format(nowTime).equals(TIME_WEATHER) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_WETH_INT) && FLAG_WEATHER == false)) {
                bot.sendMsgWeather();
                FLAG_WEATHER = true;
            }
            if (dateFormatSlach.format(nowTime).equals(TIME_NOTIFICATION) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_NOTIF_INT) && FLAG_NOTIFICATION == false)) {
                bot.sendMsgNotification();
                FLAG_NOTIFICATION = true;
            }
        } else {
            if (dateFormatSlach.format(nowTime).equals(TIME_WEEKEND) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_WEEKEND_INT) && FLAG_BIRTHDAY == false)) {
                bot.sendMsgBirthday(nowTime);
                FLAG_BIRTHDAY = true;
            }
            if (dateFormatSlach.format(nowTime).equals(TIME_WEEKEND) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_WEEKEND_INT) && FLAG_WEATHER == false)) {
                bot.sendMsgWeather();
                FLAG_WEATHER = true;
            }
            if (dateFormatSlach.format(nowTime).equals(TIME_WEEKEND) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_WEEKEND_INT) && FLAG_NOTIFICATION == false)) {
                bot.sendMsgNotification();
                FLAG_NOTIFICATION = true;
            }
        }
        if(dateFormat.format(nowTime).equals("00:00")){
            FLAG_BIRTHDAY = false;
            FLAG_WEATHER = false;
            FLAG_NOTIFICATION = false;
        }
    }
}
