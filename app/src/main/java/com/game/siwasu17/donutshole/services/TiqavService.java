package com.game.siwasu17.donutshole.services;

import com.game.siwasu17.donutshole.models.ImageEntry;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * TiqavService
 */
public interface TiqavService {
    @GET("search/newest.json")
    Call<ImageEntry[]> searchNewest();

}
