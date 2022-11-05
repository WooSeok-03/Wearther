package com.android.wearther.data.model

enum class WeatherConditions {
    THUNDERSTORM,
    DRIZZLE,
    RAIN,
    SNOW,
    FOG,
    CLEAR,
    FEW_CLOUDS,
    MANY_CLOUDS,
    CLOUDS;

    override fun toString(): String {
        return when(this) {
            THUNDERSTORM -> "뇌우"
            DRIZZLE -> "이슬비"
            RAIN -> "비"
            SNOW -> "눈"
            FOG -> "안개"
            CLEAR -> "맑음"
            FEW_CLOUDS -> "구름조금"
            MANY_CLOUDS -> "구름많음"
            CLOUDS -> "흐림"
        }
    }
}