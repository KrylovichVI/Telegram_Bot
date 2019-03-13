package com.belintersat.bot.Weather;

import com.google.common.base.Throwables;
import net.aksingh.owmjapis.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by KrylovichVI on 08.08.2017.
 */
public class Weather {
    private static final Logger logger = Logger.getLogger(Weather.class.getName());
    private static final String[] windData = {"North", "North-East", "North-East", "North-East", "East", "South-East",
                                            "South-East", "South-East", "South", "South-West", "South-West", "South-West",
                                            "West", "North-West", "North-West", "North-West", "North"};
    private static boolean isMetric = true;
    private static String owmApiKey = "60b8d6c77e0168078ed0b73b966abb50";
    private static String weatherCity = "Stankava, BY";
   // private static String weatherCity = "Stan, BY";
    private static byte forecastDays = 1;

    public static String showWeather(){
        String result = "";
        OpenWeatherMap.Units units = (isMetric) ? OpenWeatherMap.Units.METRIC : OpenWeatherMap.Units.IMPERIAL;
        OpenWeatherMap own = new OpenWeatherMap(units, OpenWeatherMap.Language.RUSSIAN, owmApiKey);


        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            DailyForecast forecast = own.dailyForecastByCityName(weatherCity, forecastDays);
            DailyForecast.Forecast dayForecast = forecast.getForecastInstance(0);
            DailyForecast.Forecast.Temperature  temperature = dayForecast.getTemperatureInstance();


            CurrentWeather currentWeather = own.currentWeatherByCityName(weatherCity);
            AbstractWeather.Weather weatherInstance = currentWeather.getWeatherInstance(0);
            CurrentWeather.Main mainInstance = currentWeather.getMainInstance();
            CurrentWeather.Sys sysInstance = currentWeather.getSysInstance();
            CurrentWeather.Wind windInstance = currentWeather.getWindInstance();
            float longitude = currentWeather.getCoordInstance().getLongitude();
            float latitude = currentWeather.getCoordInstance().getLatitude();


            result = "Weather for: " + forecast.getCityInstance().getCityName() +
                    "\nTemperature: " + temperature.getMinimumTemperature() + "°C ... " + temperature.getMaximumTemperature() + "°C" +
                    "\nTemperature Now: " + temperature.getMorningTemperature() + "°C" +
                    "\nWind: " +  calculationWindDirection(windInstance.getWindDegree()) + ", " + windInstance.getWindSpeed() + "m/s" +
                    "\nCloudiness: " + weatherInstance.getWeatherDescription() +
                    "\nHumidity: " + mainInstance.getHumidity() + "%" +
                    "\nSunrise: " + dateFormat.format(sysInstance.getSunriseTime()) +
                    "\nSunset: " + dateFormat.format(sysInstance.getSunsetTime()) +
                    "\nPressure: "  +  currentWeather.getMainInstance().getPressure() + "hpa" +
                    "\nGeo coords: " + "[" + latitude + "," + longitude + "]" +
                    "\n" + own.currentWeatherByCityName(weatherCity).getDateTime();
            logger.info("Запрос Weather сформирован ");
        } catch (IOException e) {
            String ex = Throwables.getStackTraceAsString(e);
            e.printStackTrace();
            logger.error("Запрос на Weather не сформирован " + ex);
        }
        return  result;
    }

    private static String calculationWindDirection(float degree){
        int result = (int)Math.floor(degree / 22.5);
        return windData[result];
    }
}
