package com.game.siwasu17.donutshole.services;

import com.game.siwasu17.donutshole.models.ImageEntry;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * TiqavService
 */
public interface TiqavService {
    @GET("search.json")
    Call<ImageEntry[]> search(@Query("q") String query);

    @GET("search/newest.json")
    Call<ImageEntry[]> searchNewest();

    @GET("search/random.json")
    Call<ImageEntry[]> searchRandom();
}
