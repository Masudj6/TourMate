package com.example.lazy_programmer.tourmate.Weather.newData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by lazy-programmer on 9/20/17.
 */

public interface DataAPI {

    @GET()
    Call<WeatherModel> getAtmosphereInfo(@Url String query);
}
