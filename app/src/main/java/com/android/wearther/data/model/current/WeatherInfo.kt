package com.android.wearther.data.model.current

import com.android.wearther.data.model.WeatherConditions

data class WeatherInfo(
    val weather: WeatherConditions,
    val temperature: Double,
    val wear: String,
    val humidity: Int,
    val wind: Double
)