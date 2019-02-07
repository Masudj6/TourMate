package com.example.lazy_programmer.tourmate.Weather;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lazy-programmer on 9/20/17.
 */

public class RetrofitClient {

    public static Retrofit getClient(){
        return new Retrofit.Builder().baseUrl("https://api.apixu.com/v1/").addConverterFactory(GsonConverterFactory.create()).build();
    }
}
