package com.android.wearther.presentation.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.wearther.R
import com.android.wearther.data.api.RetrofitClient.Companion.getApiService
import com.android.wearther.data.model.week.Week
import com.android.wearther.data.model.week.WeekWeather
import com.android.wearther.data.model.week.ForecastInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class ForecastViewModel: ViewModel() {

    private val forecastInfoData = MutableLiveData<List<ForecastInfo>>()
    val forecastInfo: LiveData<List<ForecastInfo>>
    get() = forecastInfoData

    fun getForecastWeather() {
        val service = getApiService()

        service.getForecastWeather().enqueue(object : Callback<WeekWeather> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<WeekWeather>, response: Response<WeekWeather>) {
                Log.i("MYTAG", "onResponse() : $response")
                if (response.code() == 200) {
                    Log.i("MYTAG", "response.body : ${response.body()?.list}")

                    val forecastList = mutableListOf<ForecastInfo>()    // 대표 날씨 List
                    for(date in response.body()?.list!!) {
                        if(date.dt_txt.contains("12:00:00")) {

                            val dateValue = date.dt_txt.replace(" 12:00:00", "")
                            val weatherValue = date.weather.firstOrNull()?.id ?: return
                            val temperature = date.main.temp
                            val wearInfo = recommendDress(temperature.roundToInt())
                            val weatherIcon = when(translateWeather(weatherValue)) {
                                "뇌우" -> R.drawable.icon_thunder
                                "이슬비" -> R.drawable.icon_drizzle
                                "비" -> R.drawable.icon_rain
                                "눈" -> R.drawable.icon_snow
                                "안개" -> R.drawable.icon_mist
                                "맑음" -> R.drawable.icon_sunny
                                "구름조금" -> R.drawable.icon_little_cloud
                                "구름많음" -> R.drawable.icon_many_cloud
                                "흐림" -> R.drawable.icon_cloudy
                                else -> R.drawable.icon_sunny
                            }

                            val forecastValue = ForecastInfo(
                                dateValue,
                                translateWeather(weatherValue),
                                temperature,
                                wearInfo,
                                weatherIcon
                            )

                            forecastList.add(forecastValue)
                        }
                    }
                    forecastInfoData.postValue(forecastList)
                }
            }

            override fun onFailure(call: Call<WeekWeather>, t: Throwable) {
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