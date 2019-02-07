package com.example.lazy_programmer.tourmate.Weather.newData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Current implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@SerializedName("last_updated_epoch")
	public int last_updated_epoch;
	
	@SerializedName("wind_degree")
	public int wind_degree;

    @SerializedName("vis_km")
    public String visibility;

	@SerializedName("humidity")
	public int humidity;
	
	@SerializedName("cloud")
	public int cloud;

    //Condition mCondition = new Condition();

    @SerializedName("condition")
    public Condition mCondition;
	
	@SerializedName("last_updated")
	public String last_updated;
	
	@SerializedName("wind_dir")
	public String wind_dir;
	
	@SerializedName("temp_c")
	public double temp_c;
	
	@SerializedName("temp_f")
	public double temp_f;
	
	@SerializedName("wind_mph")
	public double wind_mph;
	
	@SerializedName("wind_kph")
	public double wind_kph;
	
	@SerializedName("pressure_mb")
	public double pressure_mb;
	
	@SerializedName("pressure_in")
	public double pressure_in;
	
	@SerializedName("precip_mm")
	public double precip_mm;
	
	@SerializedName("precip_in")
	public double precip_in;
	
	@SerializedName("feelslike_c")
	public double feelslike_c;
	
	@SerializedName("feelslike_f")
	public double feelslike_f;
	
	


     public int getLastUpdateEpoch()
     {
     	return last_updated_epoch;
     }
     public void setLastUpdateEpoch(int mLastUpdateEpoch)
     {
     	this.last_updated_epoch = mLastUpdateEpoch;
     }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getLastUpdated()
     {
     	return last_updated;
     }
     public void setLastUpdated(String mLastUpdated)
     {
     	this.last_updated = mLastUpdated;
     }
     
     public double getTempC()
     {
     	return temp_c;
     }

     public void setTempC(Double mTempC)
     {
     	this.temp_c = mTempC;
     }
     
     public double getTempF()
     {
     	return temp_f;
     }
     public void setTempF(Double mTempF)
     {
     	this.temp_f = mTempF;
     }
     
     public Condition getCondition()
     {
     	return mCondition;
     }
     public void setCondition(Condition mCondition)
     {
     	this.mCondition = mCondition;
     }
     
     public double getWindMph()
     {
     	return wind_mph;
     }
     public void setWindMph(double mWindMph)
     {
     	this.wind_mph = mWindMph;
     }
     
     public double getWindKph()
     {
     	return wind_kph;
     }
     public void setWindKph(double mWindKph)
     {
     	this.wind_kph = mWindKph;
     }
     
     public int getWindDegree()
     {
     	return wind_degree;
     }
     public void setWindDegree(int mWindDegree)
     {
     	this.wind_degree = mWindDegree;
     }
     
     public String getWindDir()
     {
     	return wind_dir;
     }
     public void setWindDir(String mWindDir)
     {
     	this.wind_dir = mWindDir;
     }
     
     public double getPressureMb()
     {
     	return pressure_mb;
     }
     public void setPressureMb(double mPressureMb)
     {
     	this.pressure_mb = mPressureMb;
     }
     
     public double getPressureIn()
     {
     	return pressure_in;
     }
     public void setPressureIn(double mPressureIn)
     {
     	this.pressure_in = mPressureIn;
     }
     
     public double getPrecipMm()
     {
     	return precip_mm;
     }
     public void setPrecipMm(double mPrecipMm)
     {
     	this.precip_mm = mPrecipMm;
     }
     
     public double getPrecipIn()
     {
     	return precip_in;
     }
     public void setPrecipIn(double mPrecipIn)
     {
     	this.precip_in = mPrecipIn;
     }
     
     public int getHumidity()
     {
     	return humidity;
     }
     public void setHumidity(int mHumidity)
     {
     	this.humidity = mHumidity;
     }
     
     public int getCloud()
     {
     	return cloud;
     }
     public void setCloud(int mCloud)
     {
     	this.cloud = mCloud;
     }
     
     public double getFeelslikeC()
     {
     	return feelslike_c;
     }
     public void setFeelslikeC(double mFeelslikeC)
     {
     	this.feelslike_c = mFeelslikeC;
     }
     
     public double getFeelslikeF()
     {
     	return feelslike_f;
     }
     public void setFeelslikeF(double mFeelslikeF)
     {
     	this.feelslike_f = mFeelslikeF;
     }
     
}
