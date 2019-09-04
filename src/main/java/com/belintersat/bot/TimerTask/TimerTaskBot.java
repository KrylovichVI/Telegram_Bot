package com.belintersat.bot.TimerTask;
import com.belintersat.bot.Bot.Bot;
import com.belintersat.bot.Domain.FlagDAO;
import com.belintersat.bot.Domain.Flags;
import com.google.common.base.Throwables;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;



public class TimerTaskBot extends TimerTask {
    private transient Bot bot;
    private static final Logger logger = Logger.getLogger(TimerTaskBot.class.getName());

    private final int TIME_BIRTH_INT = 830;
    private final int TIME_WEEKEND_INT = 830;
    private final int TIME_WETH_INT = 700;
    private final int TIME_NOTIF_INT = 800;
    private final int TIME_GUS_INT = 900;

    private String TIME_EQUALS = "08:30";
    private String TIME_NOTIFICATION = "08:00";
    private String TIME_WEATHER = "07:00";
    private String TIME_WEEKEND = "08:30";
    private String TIME_HOLIDAYS = "07:30";
    private String TIME_NEW_YEAR = "00:01";
    private String TIME_12_APRIL = "09:30";
    private String TIME_GUS = "11:09";


    public TimerTaskBot(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        Date nowTime = new Date();
        try {
            timerTask(nowTime);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void timerTask(Date nowTime) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");
        SimpleDateFormat dateFormatSlach = new SimpleDateFormat("HH:mm");
        java.sql.Date resultDateNowTime = new java.sql.Date(nowTime.getTime());


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowTime);

        FlagDAO flagDAO = new FlagDAO();

        List<Flags> allFlags = flagDAO.getAllFlags();
        for(Flags flag : allFlags) {
            if (!resultDateNowTime.toString().equals(flag.getDate().toString())) {
                flag.setValue(0);
                flag.setDate(resultDateNowTime);
                flagDAO.updateFlag(flag);
            }
        }
        sendHoliday(calendar, dateFormatSlach, nowTime);

        if (dateFormatSlach.format(nowTime).equals(TIME_GUS) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_GUS_INT) && flagDAO.getFlagByName("FLAG_GUS").getValue() == 0)){
            bot.sendMsgGUS(calendar);
            Flags flag_gus = flagDAO.getFlagByName("FLAG_GUS");
            flag_gus.setValue(1);
            flag_gus.setDate(resultDateNowTime);
            flagDAO.updateFlag(flag_gus);
            logger.info("FLAG_GUS  изменен на true ");
        }

        if ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
            if (dateFormatSlach.format(nowTime).equals(TIME_WEEKEND) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_WEEKEND_INT) && flagDAO.getFlagByName("FLAG_BIRTHDAY").getValue() == 0)) {
                bot.sendMsgBirthday(nowTime);
                Flags flag_birthday = flagDAO.getFlagByName("FLAG_BIRTHDAY");
                flag_birthday.setValue(1);
                flag_birthday.setDate(resultDateNowTime);
                flagDAO.updateFlag(flag_birthday);
                logger.info("FLAG_BIRTHDAY  изменен на true ");
            }
            if (dateFormatSlach.format(nowTime).equals(TIME_WEEKEND) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_WEEKEND_INT) && flagDAO.getFlagByName("FLAG_WEATHER").getValue() == 0)) {
                bot.sendMsgWeather();
                Flags flag_weather = flagDAO.getFlagByName("FLAG_WEATHER");
                flag_weather.setValue(1);
                flag_weather.setDate(resultDateNowTime);
                flagDAO.updateFlag(flag_weather);
                logger.info("FLAG_WEATHER  изменен на true ");
            }
            if (dateFormatSlach.format(nowTime).equals(TIME_WEEKEND) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_WEEKEND_INT) && flagDAO.getFlagByName("FLAG_NOTIFICATION").getValue() == 0)) {
                bot.sendMsgNotification();
                Flags flag_notification = flagDAO.getFlagByName("FLAG_NOTIFICATION");
                flag_notification.setValue(1);
                flag_notification.setDate(resultDateNowTime);
                flagDAO.updateFlag(flag_notification);
                logger.info("FLAG_NOTIFICATION  изменен на true ");
            }
            } else {
                if (dateFormatSlach.format(nowTime).equals(TIME_EQUALS) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_BIRTH_INT) &&flagDAO.getFlagByName("FLAG_BIRTHDAY").getValue() == 0)) {
                    bot.sendMsgBirthday(nowTime);
                    Flags flag_birthday = flagDAO.getFlagByName("FLAG_BIRTHDAY");
                    flag_birthday.setValue(1);
                    flag_birthday.setDate(resultDateNowTime);
                    flagDAO.updateFlag(flag_birthday);
                }
                if (dateFormatSlach.format(nowTime).equals(TIME_WEATHER) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_WETH_INT) && flagDAO.getFlagByName("FLAG_WEATHER").getValue() == 0)) {
                    bot.sendMsgWeather();
                    Flags flag_weather = flagDAO.getFlagByName("FLAG_WEATHER");
                    flag_weather.setValue(1);
                    flag_weather.setDate(resultDateNowTime);
                    flagDAO.updateFlag(flag_weather);
                }
                if (dateFormatSlach.format(nowTime).equals(TIME_NOTIFICATION) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_NOTIF_INT) && flagDAO.getFlagByName("FLAG_NOTIFICATION").getValue() == 0)) {
                    bot.sendMsgNotification();
                    Flags flag_notification = flagDAO.getFlagByName("FLAG_NOTIFICATION");
                    flag_notification.setValue(1);
                    flag_notification.setDate(resultDateNowTime);
                    flagDAO.updateFlag(flag_notification);
                }
            }
//        System.out.println(dateFormatSlach.format(nowTime));
    }


    private void sendHoliday(Calendar calendar, DateFormat dateFormatSlach, Date nowTime){
        if (calendar.get(Calendar.DATE) == 1 && calendar.getTime().getMonth() == Calendar.JANUARY && dateFormatSlach.format(nowTime).equals(TIME_NEW_YEAR)) {
            String text = "Сегодня — первая страница из 365 страниц книги. Напишите её хорошо.\n" +
                    "С Новым годом всех!\n" +
                    "\nSponge Bot и команда Belintersat";
            bot.sendMsgChristmas(text);
        }else if (calendar.get(Calendar.DATE) == 7 && calendar.getTime().getMonth() == Calendar.JANUARY && dateFormatSlach.format(nowTime).equals(TIME_HOLIDAYS)) {
            String text = " Пусть Рождество войдет в ваш дом, \nС собой неся все то, что свято! \nПусть будет смех и радость в нем," +
                    "От счастья и душа богата!\n \nПускай уютом дышит дом, \nПусть ангел вас оберегает!\n" +
                    "Мы поздравляем с Рождеством \nИ только лучшего желаем!\n" +
                    "\nSponge Bot и команда Belintersat";
            bot.sendMsgChristmas(text);
        } else if(calendar.get(Calendar.DATE) == 23 && calendar.getTime().getMonth() == Calendar.FEBRUARY && dateFormatSlach.format(nowTime).equals(TIME_HOLIDAYS) ){
            bot.sendMsg23thFebruary();
        } else if(calendar.get(Calendar.DATE) == 8 && calendar.getTime().getMonth() == Calendar.MARCH && dateFormatSlach.format(nowTime).equals(TIME_HOLIDAYS) ){
            bot.sendMsg8thMarch();
        } else if(calendar.get(Calendar.DATE) == 16 && calendar.getTime().getMonth() == Calendar.JANUARY && dateFormatSlach.format(nowTime).equals(TIME_HOLIDAYS) ){
            bot.sendMsgBelintersat();
        } else if(calendar.get(Calendar.DATE) == 12 && calendar.getTime().getMonth() == Calendar.APRIL && dateFormatSlach.format(nowTime).equals(TIME_12_APRIL)){
            bot.sendMsgApril_12();
        }
    }
}



