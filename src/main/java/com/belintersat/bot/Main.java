package com.belintersat.bot;
import com.belintersat.bot.Bot.Bot;
import com.belintersat.bot.TimerTask.TimerTaskBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import java.util.Timer;


public class Main {
    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        Bot bot = new Bot();
        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
        timerBirthday(bot);

    }

    public static void timerBirthday(Bot bot){
        TimerTaskBot timerTask = new TimerTaskBot(bot);
        Timer timer = new Timer();
        timer.schedule(timerTask, 3000, 1000 * 60);
    }

}
