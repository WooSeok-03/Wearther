package com.android.wearther.presentation.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.wearther.data.api.RetrofitClient.Companion.getApiService
import com.android.wearther.data.model.week.Week
import com.android.wearther.data.model.week.WeekWeather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForecastViewModel: ViewModel() {

    var forecastData = MutableLiveData<List<Week>>()
    val forecast : LiveData<List<Week>>
    get() = forecastData

    fun getForecastWeather() {
        val service = getApiService()

        service.getForecastWeather().enqueue(object : Callback<WeekWeather> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<WeekWeather>, response: Response<WeekWeather>) {
                Log.i("MYTAG", "onResponse() : $response")
                if (response.code() == 200) {
                    Log.i("MYTAG", "response.body : ${response.body()?.list}")

                    var forecastList = mutableListOf<Week>()    // 대표 날씨 List
                    for(date in response.body()?.list!!) {
                        // 12시를 기준
                        if(date.dt_txt.contains("12:00:00")) {
                            forecastList.add(date)
                        }
                    }

                    forecastData.postValue(forecastList)
                }
            }

            override fun onFailure(call: Call<WeekWeather>, t: Throwable) {
                Log.i("MYTAG", t.localizedMessage)
            }

        })
    }
}