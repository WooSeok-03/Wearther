package com.android.wearther.data.model.week

data class ForecastInfo(
    val date: String,
    val weather: String,
    val temperature: Double,
    val wear: String,
    val icon: Int
): java.io.Serializable