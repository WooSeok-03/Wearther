package com.android.wearther.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.wearther.BuildConfig
import com.android.wearther.data.api.WeatherService
import com.android.wearther.data.model.Weather
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

    fun getCurrentWeather() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherService::class.java)

        service.getCurrentWeather().enqueue(object : Callback<Weather>{
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                Log.i("MYTAG", "onResponse() : $response")
                if(response.code() == 200) {
                    Log.i("MYTAG", "response.body : ${response.body()}")

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
}

