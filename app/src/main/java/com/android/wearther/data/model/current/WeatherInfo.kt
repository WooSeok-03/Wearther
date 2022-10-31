package com.android.wearther.data.model.current

data class WeatherInfo(
    val weather: String,
    val temperature: Double,
    val wear: String,
    val humidity: Int,
    val wind: Double
)