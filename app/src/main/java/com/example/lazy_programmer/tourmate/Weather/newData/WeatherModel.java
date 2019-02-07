package com.example.lazy_programmer.tourmate.Weather.newData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WeatherModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SerializedName("location")
    Locations locations;
	
	@SerializedName("current")
	Current current;
	
	@SerializedName("forecast")
	Forecast forecast;
	
     public Locations getLocations()
     {
    	 return locations;
     }
     public void setLocations(Locations mLocations)
     {
    	 this.locations = mLocations;
     }
     
     public Current getCurrent()
     {
    	 return current;
     }
     public void setCurrent(Current mCurrent)
     {
    	 this.current = mCurrent;
     }
     
     public Forecast getForecast()
     {
    	 return forecast;
     }
     public void setForecast(Forecast mForecast)
     {
    	 this.forecast = mForecast;
     }

}
