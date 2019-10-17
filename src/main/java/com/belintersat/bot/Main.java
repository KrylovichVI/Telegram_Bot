package com.belintersat.bot;

import com.belintersat.bot.Bot.Bot;
import com.belintersat.bot.HibernateUtil.HibernateUtil;
import com.belintersat.bot.TimerTask.TimerTaskBot;
import com.google.common.base.Throwables;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.Timer;


public class Main {
    private final static Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) throws IOException {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        Bot bot = new Bot();
        try {
             telegramBotsApi.registerBot(bot);
            logger.info("Бот запущен ");
        } catch (TelegramApiRequestException e) {
            String ex = Throwables.getStackTraceAsString(e);
            e.printStackTrace();
            logger.error("Бот не запущен " + ex);
        }
        timerNotification(bot);
    }

    public static void timerNotification(Bot bot){
        System.out.println("Start");
        TimerTaskBot timerTask = null;
        if(timerTask == null){
            timerTask = new TimerTaskBot(bot);
        }
        Timer timer = new Timer();
        timer.schedule(timerTask, 3000, 1000 * 60);


    }

}
