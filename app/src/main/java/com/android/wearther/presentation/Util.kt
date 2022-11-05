package com.android.wearther.presentation

import com.android.wearther.R
import com.android.wearther.data.model.WeatherConditions

object Util {

    fun getIconFromWeather(weather: WeatherConditions) : Int {
        return when(weather) {
            WeatherConditions.THUNDERSTORM -> R.drawable.icon_thunder
            WeatherConditions.DRIZZLE -> R.drawable.icon_drizzle
            WeatherConditions.RAIN -> R.drawable.icon_rain
            WeatherConditions.SNOW -> R.drawable.icon_snow
            WeatherConditions.FOG -> R.drawable.icon_mist
            WeatherConditions.CLEAR -> R.drawable.icon_sunny
            WeatherConditions.FEW_CLOUDS -> R.drawable.icon_little_cloud
            WeatherConditions.MANY_CLOUDS -> R.drawable.icon_many_cloud
            WeatherConditions.CLOUDS -> R.drawable.icon_cloudy
        }
    }

    fun translateWeather(weatherCode: Int) : WeatherConditions {
        return when (weatherCode) {
            in 200..299 -> WeatherConditions.THUNDERSTORM
            in 300..499 -> WeatherConditions.DRIZZLE
            in 500..599 -> WeatherConditions.RAIN
            in 600..699 -> WeatherConditions.SNOW
            in 701..799 -> WeatherConditions.FOG
            800 -> WeatherConditions.CLEAR
            801 -> WeatherConditions.FEW_CLOUDS
            802 -> WeatherConditions.MANY_CLOUDS
            in 803..804 -> WeatherConditions.CLOUDS
            else -> WeatherConditions.CLEAR
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