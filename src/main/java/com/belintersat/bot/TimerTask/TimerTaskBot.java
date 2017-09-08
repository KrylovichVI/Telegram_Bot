package com.belintersat.bot.TimerTask;
import com.belintersat.bot.Bot.Bot;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class TimerTaskBot extends TimerTask {
    private Bot bot;
    private String TIME_EQUALS  = "08:30";
    private String TIME_WEATHER  = "07:00";


    public TimerTaskBot(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        Date nowTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        System.out.println(dateFormat.format(nowTime));
        if(dateFormat.format(nowTime).equals(TIME_EQUALS)) {
            bot.sendMsgBirthday(nowTime);
        }
        if(dateFormat.format(nowTime).equals(TIME_WEATHER)){
            bot.sendMsgWeather();
        }
    }
}
