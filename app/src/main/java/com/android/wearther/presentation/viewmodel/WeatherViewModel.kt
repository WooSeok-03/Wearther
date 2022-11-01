package com.android.wearther.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.wearther.data.api.RetrofitClient.Companion.getApiService
import com.android.wearther.data.model.current.Weather
import com.android.wearther.data.model.current.WeatherInfo
import retrofit2.*
import kotlin.math.roundToInt

class WeatherViewModel : ViewModel() {

    private val weatherInfoData = MutableLiveData<WeatherInfo>()
    val weatherInfo: LiveData<WeatherInfo>
        get() = weatherInfoData

    fun getCurrentWeather() {
        val service = getApiService()

        service.getCurrentWeather().enqueue(object : Callback<Weather> {
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                Log.i("MYTAG", "onResponse() : $response")
                if (response.code() == 200) {
                    Log.i("MYTAG", "response.body : ${response.body()}")

                    val weatherList = response.body()?.weather ?: return

                    val weatherValue = weatherList.firstOrNull()?.id ?: return
                    val temperature = response.body()?.main?.temp ?: 0.0
                    val wearInfo = recommendDress(temperature.roundToInt())
                    val humidityValue = response.body()?.main?.humidity ?: 0
                    val windValue = response.body()?.wind?.speed ?: 0.0

                    val weatherInfo = WeatherInfo(
                        translateWeather(weatherValue),
                        temperature,
                        wearInfo,
                        humidityValue,
                        windValue
                    )
                    weatherInfoData.postValue(weatherInfo)

                } else {
                    Log.i("MYTAG", "Error : $response")
                }
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                Log.i("MYTAG", t.localizedMessage)
            }
        })
    }

    fun translateWeather(weather: Int): String {
        return when (weather) {
            in 200..299 -> "뇌우"
            in 300..499 -> "이슬비"
            in 500..599 -> "비"
            in 600..699 -> "눈"
            in 701..799 -> "안개"
            800 -> "맑음"
            801 -> "구름조금"
            802 -> "구름많음"
            in 803..804 -> "흐림"
            else -> ""
        }
    }

    fun recommendDress(currentTemperature: Int): String {
        val clothes: String =
            if (currentTemperature >= 28) {               // 28℃ ~
                "민소매, 반바지, 반팔, \n치마, 원피스"
            } else if (currentTemperature in 23..27) { // 23℃ ~ 27℃
                "반팔, 얇은 셔츠, \n반바지, 면바지"
            } else if (currentTemperature in 20..22) { // 20℃ ~ 22℃
                "긴팔, 얇은 가디건, \n면바지, 청바지"
            } else if (currentTemperature in 17..19) { // 17℃ ~ 19℃
                "얇은 니트, 얇은 재킷, \n가디건, 맨투맨, 면바지, 청바지"
            } else if (currentTemperature in 12..16) { // 12℃ ~ 16℃
                "얇은 재킷, 가디건, 야상, 맨투맨, \n니트, 청바지, 면바지, 살구색 스타킹"
            } else if (currentTemperature in 9..11) {  // 9℃ ~ 11℃
                "재킷, 트랜치 코트, 야상, \n니트, 면바지, 청바지, 검은색 스타킹"
            } else if (currentTemperature in 5..8) {   // 5℃ ~ 8℃
                "코트, 가죽 재킷, 니트, \n스카프, 두꺼운 바지, 히트텍"
            } else {                                // ~ 4℃
                "패딩, 두꺼운 코트, \n목도리, 기모 제품"
            }

        return clothes
    }

}

