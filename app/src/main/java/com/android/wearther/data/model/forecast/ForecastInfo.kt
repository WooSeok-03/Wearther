package com.android.wearther.data.model.forecast

import com.android.wearther.data.model.WeatherConditions

data class ForecastInfo(
    val date: String,
    val weather: WeatherConditions,
    val temperature: Double,
    val wear: String,
    val icon: Int
): java.io.Serializable