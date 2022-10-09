package com.android.wearther.data.model.week

data class WeekWeather(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<>,
    val message: Int
)