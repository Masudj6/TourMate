package com.example.lazy_programmer.tourmate.NearBy;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lazy-programmer on 9/20/17.
 */

public class RetrofitClientPlace {

    public static Retrofit getClient(){
        return new Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/place/").addConverterFactory(GsonConverterFactory.create()).build();
    }
}
