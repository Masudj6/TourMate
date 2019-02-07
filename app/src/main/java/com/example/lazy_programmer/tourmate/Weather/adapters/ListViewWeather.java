package com.example.lazy_programmer.tourmate.Weather.adapters;


import com.example.lazy_programmer.tourmate.Weather.newData.Forecastday;
import com.example.lazy_programmer.tourmate.Weather.newData.WeatherModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListViewWeather {

    private String day,maxTemp,minTemp,icon;


    public ListViewWeather(String day, String maxTemp, String minTemp, String icon) {
        this.day = day;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.icon = icon;
    }

    public ListViewWeather() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ArrayList<ListViewWeather> getForecastList(WeatherModel weatherModel) throws ParseException {
        ArrayList<ListViewWeather> weatherArrayList=new ArrayList<>();
        for (Forecastday d: weatherModel.getForecast().getForecastday()) {
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inFormat.parse(d.getDate().toString());
            weatherArrayList.add(new ListViewWeather(android.text.format.DateFormat.format("EEEE",date).toString(),""+(int)d.getDay().getMaxtempC()+(char) 0x00B0,""+(int)d.getDay().getMintempC()+(char) 0x00B0,d.getDay().getCondition().getIcon().toString()));
            //Log.d("Icon: ",d.getDay().getCondition().getIcon().toString());
        }


        return weatherArrayList;
    }
}
