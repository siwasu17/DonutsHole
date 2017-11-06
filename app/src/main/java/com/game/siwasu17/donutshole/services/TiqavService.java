package com.game.siwasu17.donutshole.services;

import com.game.siwasu17.donutshole.models.TiqavImageEntry;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * TiqavService
 */
public interface TiqavService {
    @GET("search.json")
    Observable<TiqavImageEntry[]> search(@Query("q") String query);

    @GET("search/newest.json")
    Observable<TiqavImageEntry[]> searchNewest();

    @GET("search/random.json")
    Observable<TiqavImageEntry[]> searchRandom();
}
