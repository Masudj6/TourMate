package com.example.lazy_programmer.tourmate.NearBy.DataDetails;

import com.example.lazy_programmer.tourmate.NearBy.NearbyJSON.Data.Main;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by lazy-programmer on 9/20/17.
 */

public interface DataAPI {

    @GET()
    Call<MainDetails> getPlaceData(@Url String query);
}
