package com.android.wearther.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.wearther.data.api.RetrofitClient.Companion.getApiService
import com.android.wearther.data.model.current.Weather
import retrofit2.*
import kotlin.math.roundToInt

class WeatherViewModel: ViewModel(){

    var temperatureData = MutableLiveData<String>()
    val temperature: LiveData<String>
    get() = temperatureData

    var weatherData = MutableLiveData<String>()
    val weather: LiveData<String>
    get() = weatherData

    var humidityData = MutableLiveData<String>()
    val humidity : LiveData<String>
    get() = humidityData

    var windData = MutableLiveData<String>()
    val wind : LiveData<String>
    get() = windData

    fun getCurrentWeather() {
        val service = getApiService()

        service.getCurrentWeather().enqueue(object : Callback<Weather>{
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                Log.i("MYTAG", "onResponse() : $response")
                if(response.code() == 200) {
                    Log.i("MYTAG", "response.body : ${response.body()}")

                    // Current Weather
                    val weatherList = response.body()?.weather
                    if (weatherList != null) {
                        Log.i("MYTAG", "$weatherList")
                        val weatherState = weatherList.first().main
                        weatherData.postValue(weatherState)
                    }

                    // Current Temperature
                    val temperatureValue: Double = response.body()?.main?.temp_max ?: 0.0
                    temperatureData.postValue(temperatureValue.roundToInt().toString() + " ℃")

                    // Current Humidity
                    val humidityValue = response.body()?.main?.humidity
                    humidityData.postValue(humidityValue.toString() + "%")

                    // Current Wind
                    val windValue = response.body()?.wind?.speed
                    windData.postValue(windValue.toString() + "m/s")

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

