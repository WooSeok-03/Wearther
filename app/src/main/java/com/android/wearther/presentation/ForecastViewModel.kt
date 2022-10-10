package com.android.wearther.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.wearther.data.api.RetrofitClient.getApiService
import com.android.wearther.data.model.week.WeekWeather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForecastViewModel: ViewModel() {

    fun getForecastWeather() {
        val service = getApiService()

        service.getForecastWeather().enqueue(object : Callback<WeekWeather> {
            override fun onResponse(call: Call<WeekWeather>, response: Response<WeekWeather>) {
                Log.i("MYTAG", "onResponse() : $response")
                if (response.code() == 200) {
                    Log.i("MYTAG", "response.body : ${response.body()?.list}")

                    for(i in response.body()?.list!!) {
                        Log.i("MYTAG", "$i")
                    }
                    Log.i("MYTAG", "${response.body()?.list?.size}")

                }
            }

            override fun onFailure(call: Call<WeekWeather>, t: Throwable) {
                Log.i("MYTAG", t.localizedMessage)
            }

        })
    }
}