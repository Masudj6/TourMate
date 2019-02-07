package com.example.lazy_programmer.tourmate.Weather.adapters;

import com.example.lazy_programmer.tourmate.Weather.newData.Hour;
import com.example.lazy_programmer.tourmate.Weather.newData.WeatherModel;

import java.util.ArrayList;

public class RecyclerWeather {

    private String localTime,humidity,icon;


    public RecyclerWeather(String localTime, String humidity, String icon) {
        this.localTime = localTime;
        this.humidity = humidity;
        this.icon = icon;
    }

    public RecyclerWeather() {
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        this.localTime = localTime;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ArrayList<RecyclerWeather> getHourlyForecastList(WeatherModel weatherModel){
        ArrayList<RecyclerWeather> weatherArrayList=new ArrayList<>();
        for (Hour hour:weatherModel.getForecast().getForecastday().get(0).getHour()) {
            String[] time=hour.getTime().split(" ");
            weatherArrayList.add(new RecyclerWeather(time[1],""+(int)hour.getTempC()+(char) 0x00B0,hour.getCondition().getIcon().toString()));
        }

        return weatherArrayList;
    }
}


