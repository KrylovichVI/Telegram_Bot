package com.belintersat.bot.TimerTask;
import com.belintersat.bot.Bot.Bot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

public class TimerTaskBot extends TimerTask {
    enum DAYS_OF_WEEK  {MONDEY, TUESDAY, WEDNESDAY, THUERSDAY, FRIDAY, SATURDAY, SUNDAY}
    private Bot bot;
    private final int TIME_BIRTH_INT = 830;
    private final int TIME_WETH_INT = 700;
    private String TIME_EQUALS  = "08:30";
    private String TIME_WEATHER  = "07:00";
    private boolean FLAG_BIRTHDAY = false;
    private boolean FLAG_WEATHER = false;


    public TimerTaskBot(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        Date nowTime = new Date();
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getTime().getMinutes() );
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");
        SimpleDateFormat dateFormatSlach = new SimpleDateFormat("HH:mm");
        System.out.println(dateFormatSlach.format(nowTime));
        if(dateFormatSlach.format(nowTime).equals(TIME_EQUALS) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_BIRTH_INT) && FLAG_BIRTHDAY == false)) {
            bot.sendMsgBirthday(nowTime);
            FLAG_BIRTHDAY = true;
        }
        if(dateFormatSlach.format(nowTime).equals(TIME_WEATHER) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_WETH_INT) && FLAG_WEATHER == false)){
            bot.sendMsgWeather();
            FLAG_WEATHER = true;
        }
        if(dateFormat.format(nowTime).equals("00:00")){
            FLAG_BIRTHDAY = false;
            FLAG_WEATHER = false;
        }
    }
}
