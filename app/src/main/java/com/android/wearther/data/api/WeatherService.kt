package com.android.wearther.data.api

import com.android.wearther.BuildConfig
import com.android.wearther.data.model.Weather
import com.android.wearther.data.model.WeatherX
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather?")
    fun getCurrentWeather(
        @Query("id") city: String = "1835841",      // Republic of Korea
        @Query("units") units: String = "metric",   // 국제 표준 단위
        @Query("appid") appid: String = BuildConfig.API_KEY
    ): Call<Weather>

}