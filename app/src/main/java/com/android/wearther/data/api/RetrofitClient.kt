package com.android.wearther.data.api

import com.android.wearther.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        private val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        private val service: WeatherService = retrofit.create(WeatherService::class.java)

        fun getApiService(): WeatherService = service
    }
}