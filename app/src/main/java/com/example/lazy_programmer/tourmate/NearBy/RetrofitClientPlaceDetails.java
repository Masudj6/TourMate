package com.example.lazy_programmer.tourmate.NearBy;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lazy-Programmer on 10/26/2017.
 */


public class RetrofitClientPlaceDetails {

    public static Retrofit getClient(){
        return new Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/place/details/").addConverterFactory(GsonConverterFactory.create()).build();
    }
}
