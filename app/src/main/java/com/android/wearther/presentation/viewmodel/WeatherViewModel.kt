package com.android.wearther.presentation.viewmodel

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

    var wearData = MutableLiveData<String>()
    val wear : LiveData<String>
    get() = wearData

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

                    // Recommend Dress
                    wearData.postValue(recommendDress(temperatureValue.roundToInt()))

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

    fun recommendDress(currentTemperature: Int) : String {
        lateinit var clothes: String

        if(currentTemperature >= 27) {               // 27℃ ~
            clothes = "민소매, 반바지, 반팔, 치마, 원피스"
        } else if (currentTemperature in 23..26) { // 23℃ ~ 26℃
            clothes = "반팔, 얇은 셔츠, 반바지, 면바지"
        } else if (currentTemperature in 20..22) { // 20℃ ~ 22℃
            clothes = "긴팔, 얇은 가디건, 면바지, 청바지"
        } else if (currentTemperature in 17..19) { // 17℃ ~ 19℃
            clothes = "얇은 니트, 얇은 재킷, 가디건, 맨투맨, 면바지, 청바지"
        } else if (currentTemperature in 12..16) { // 12℃ ~ 16℃
            clothes = "얇은 재킷, 가디건, 야상, 맨투맨, 니트, 청바지, 면바지, 살구색 스타킹"
        } else if (currentTemperature in 9..11) {  // 9℃ ~ 11℃
            clothes = "재킷, 트랜치 코트, 야상, 니트, 면바지, 청바지, 검은색 스타킹"
        } else if (currentTemperature in 5..8) {   // 5℃ ~ 8℃
            clothes = "코트, 가죽 재킷, 니트, 스카프, 두꺼운 바지, 히트텍"
        } else {                                // ~ 4℃
            clothes = "패딩, 두꺼운 코트, 목도리, 기모 제품"
        }

        return clothes
    }


}

