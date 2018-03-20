package com.belintersat.bot.SingletonLog;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Синглетный класс - для написания собственных логов.
 */
public class SingletonLog {
    private static SingletonLog instance = null;
    private static FileHandler handler;

    private SingletonLog(){
        try {
            handler = new FileHandler("%h/myJavaLog.log", true);
            handler.setFormatter(new MyFormat());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SingletonLog getInstance(){
        if(instance == null) {
            instance = new SingletonLog();
        }
        return instance;
    }

    public static FileHandler getHandler() {
        return handler;
    }

    static class MyFormat extends Formatter{
        @Override
        public String format(LogRecord record) {
            return  "\n"+ record.getLevel() + ": " + new Date(record.getMillis()) + " "  + record.getSourceClassName()  + " METHOD: " + record.getSourceMethodName();
        }
    }
}
