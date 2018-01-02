package com.edipo.uni7java.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {

    @GET("data/2.5/weather")
    Call<WeatherRsp> getWeather(@Query("lat") double latitude,
                                @Query("lon") double longitude,
                                @Query("lang") String language,
                                @Query("units") String units,
                                @Query("appid") String appId);

}
