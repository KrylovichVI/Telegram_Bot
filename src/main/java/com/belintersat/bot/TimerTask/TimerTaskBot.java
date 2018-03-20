package com.belintersat.bot.TimerTask;
import com.belintersat.bot.Bot.Bot;
import com.google.common.base.Throwables;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimerTask;

public class TimerTaskBot extends TimerTask {
    private transient Bot bot;
    private static final Logger logger = Logger.getLogger(TimerTaskBot.class.getName());

    private final int TIME_BIRTH_INT = 830;
    private final int TIME_WEEKEND_INT = 830;
    private final int TIME_WETH_INT = 700;
    private final int TIME_NOTIF_INT = 800;

    private String TIME_EQUALS = "08:30";
    private String TIME_NOTIFICATION = "08:00";
    private String TIME_WEATHER = "07:00";
    private String TIME_WEEKEND = "08:30";
    private String TIME_HOLIDAYS = "07:30";


    public TimerTaskBot(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        Date nowTime = new Date();
        timerTask(nowTime);
    }

    private void timerTask(Date nowTime){

        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");
        SimpleDateFormat dateFormatSlach = new SimpleDateFormat("HH:mm");
        java.sql.Date resultDate = null;
        java.sql.Date resultDateNowTime = new java.sql.Date(nowTime.getTime());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowTime);


        System.out.println(resultDateNowTime);
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            String ex = Throwables.getStackTraceAsString(e);
            logger.error("Не удалось подключиться к JDBC Driver " + ex);
            e.printStackTrace();
        }


        try(Connection connection = DriverManager.getConnection(getProperties("url"), getProperties("login"), getProperties("password"));
            Statement statement = connection.createStatement()) {
            boolean FLAG_WEATHER = false, FLAG_NOTIFICATION = false, FLAG_BIRTHDAY = false;
            ResultSet resultSet = statement.executeQuery("SELECT name, value, date FROM flags");
            while(resultSet.next()){
                switch (resultSet.getString("name")){
                    case "FLAG_WEATHER" : {
                        FLAG_WEATHER = resultSet.getBoolean("value");
                        break;
                    }
                    case "FLAG_NOTIFICATION" : {
                        FLAG_NOTIFICATION = resultSet.getBoolean("value");
                        break;
                    }
                    case "FLAG_BIRTHDAY" : {
                        FLAG_BIRTHDAY = resultSet.getBoolean("value");
                        break;
                    }
                    default: break;
                }
                resultDate = resultSet.getDate("date");

            }

            if (!resultDateNowTime.toString().equals(resultDate.toString())){
                if(statement.execute("UPDATE flags SET value = false, date = CURRENT_DATE ")) {
                    logger.info("Флаги изменены ");
                } else{
                    logger.error("Флаги не поменялись");
                }

            }
            sendHoliday(calendar, dateFormatSlach, nowTime);

            if ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                if (dateFormatSlach.format(nowTime).equals(TIME_WEEKEND) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_WEEKEND_INT) && FLAG_BIRTHDAY == false)) {
                    bot.sendMsgBirthday(nowTime);
                    statement.execute("UPDATE flags SET value = true WHERE name = 'FLAG_BIRTHDAY'");
                    logger.info("FLAG_BIRTHDAY  изменен на true ");
                }
                if (dateFormatSlach.format(nowTime).equals(TIME_WEEKEND) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_WEEKEND_INT) && FLAG_WEATHER == false)) {
                    bot.sendMsgWeather();
                    statement.execute("UPDATE flags SET value = true WHERE name = 'FLAG_WEATHER'");
                    logger.info("FLAG_WEATHER  изменен на true ");
                }
                if (dateFormatSlach.format(nowTime).equals(TIME_WEEKEND) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_WEEKEND_INT) && FLAG_NOTIFICATION == false)) {
                    bot.sendMsgNotification();
                    statement.execute("UPDATE flags SET value = true WHERE name = 'FLAG_NOTIFICATION'");
                    logger.info("FLAG_NOTIFICATION  изменен на true ");
                }
            } else {
                if (dateFormatSlach.format(nowTime).equals(TIME_EQUALS) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_BIRTH_INT) && FLAG_BIRTHDAY == false)) {
                    bot.sendMsgBirthday(nowTime);
                    statement.execute("UPDATE flags SET value = true WHERE name = 'FLAG_BIRTHDAY'");
                    logger.info("FLAG_BIRTHDAY  изменен на true ");
                }
                if (dateFormatSlach.format(nowTime).equals(TIME_WEATHER) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_WETH_INT) && FLAG_WEATHER == false)) {
                    bot.sendMsgWeather();
                    statement.execute("UPDATE flags SET value = true WHERE name = 'FLAG_WEATHER'");
                    logger.info("FLAG_WEATHER  изменен на true ");
                }
                if (dateFormatSlach.format(nowTime).equals(TIME_NOTIFICATION) || (Integer.valueOf(dateFormat.format(nowTime)) > (TIME_NOTIF_INT) && FLAG_NOTIFICATION == false)) {
                    bot.sendMsgNotification();
                    statement.execute("UPDATE flags SET value = true WHERE name = 'FLAG_NOTIFICATION'");
                    logger.info("FLAG_NOTIFICATION  изменен на true ");
                }
            }

            System.out.println(dateFormatSlach.format(nowTime));
        }catch(SQLException ex){
            String e = Throwables.getStackTraceAsString(ex);
            logger.error("Не удается получить connection " + e);
            ex.printStackTrace();
        }
    }

    private String getProperties(String object){

        Properties properties = System.getProperties();
        InputStream fileInputStream = getClass().getClassLoader().getResourceAsStream("properties/db.properties");
        try {
            properties.load(fileInputStream);
        } catch (IOException e) {
            String ex = Throwables.getStackTraceAsString(e);
            e.printStackTrace();
            logger.error("Properties не подгружены " + ex);
        }

        return properties.getProperty(object);
    }

    private void sendHoliday(Calendar calendar, DateFormat dateFormatSlach, Date nowTime){
        if (calendar.get(Calendar.DATE) == 7 && calendar.getTime().getMonth() == Calendar.JANUARY && dateFormatSlach.format(nowTime).equals(TIME_HOLIDAYS)) {
            bot.sendMsgChristmas();
        } else if(calendar.get(Calendar.DATE) == 23 && calendar.getTime().getMonth() == Calendar.FEBRUARY && dateFormatSlach.format(nowTime).equals(TIME_HOLIDAYS) ){
            bot.sendMsg23thFebruary();
        } else if(calendar.get(Calendar.DATE) == 8 && calendar.getTime().getMonth() == Calendar.MARCH && dateFormatSlach.format(nowTime).equals(TIME_HOLIDAYS) ){
            bot.sendMsg8thMarch();
        } else if(calendar.get(Calendar.DATE) == 16 && calendar.getTime().getMonth() == Calendar.JANUARY && dateFormatSlach.format(nowTime).equals(TIME_HOLIDAYS) ){
            bot.sendMsgBelintersat();
        }
    }
}



