package com.android.wearther.data.model.forecast

data class WeekWeather(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Week>,
    val message: Int
)