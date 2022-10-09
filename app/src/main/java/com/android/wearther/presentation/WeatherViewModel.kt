package com.android.wearther.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.wearther.BuildConfig
import com.android.wearther.data.api.WeatherService
import com.android.wearther.data.model.current.Weather
import com.android.wearther.data.model.week.WeekWeather
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

class WeatherViewModel: ViewModel(){

    var temperatureData = MutableLiveData<String>()
    val temperature: LiveData<String>
    get() = temperatureData

    var maxTemperatureData = MutableLiveData<String>()
    val maxTemperature: LiveData<String>
    get() = maxTemperatureData

    var minTemperatureData = MutableLiveData<String>()
    val minTemperature: LiveData<String>
    get() = minTemperatureData

    private fun setRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    fun getCurrentWeather() {
        val service = setRetrofit().create(WeatherService::class.java)

        service.getCurrentWeather().enqueue(object : Callback<Weather>{
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                //Log.i("MYTAG", "onResponse() : $response")
                if(response.code() == 200) {
                    //Log.i("MYTAG", "response.body : ${response.body()}")

                    val temp: Double = response.body()?.main?.temp_max ?: 0.0
                    val maxTemp: Double = response.body()?.main?.temp_max ?: 0.0
                    val minTemp: Double = response.body()?.main?.temp_min ?: 0.0

                    temperatureData.postValue("Temperature : " + temp.roundToInt().toString() + " ℃")
                    maxTemperatureData.postValue("Max Temperature : " + maxTemp.roundToInt().toString() + " ℃")
                    minTemperatureData.postValue("Min Temperature : " + minTemp.roundToInt().toString() + " ℃")

                } else {
                    Log.i("MYTAG", "Error : $response")
                }
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                Log.i("MYTAG", t.localizedMessage)
            }
        })
    }


    fun getForecastWeather() {
        val service = setRetrofit().create(WeatherService::class.java)

        service.getForecastWeather().enqueue(object : Callback<WeekWeather>{
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

