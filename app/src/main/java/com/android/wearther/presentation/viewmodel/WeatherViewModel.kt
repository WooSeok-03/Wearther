package com.android.wearther.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.wearther.data.api.RetrofitClient.Companion.getApiService
import com.android.wearther.data.model.current.Weather
import com.android.wearther.data.model.current.WeatherInfo
import com.android.wearther.presentation.Util
import retrofit2.*
import kotlin.math.roundToInt

class WeatherViewModel : BaseViewModel() {

    private val weatherInfoData = MutableLiveData<WeatherInfo>()
    val weatherInfo: LiveData<WeatherInfo>
        get() = weatherInfoData

    fun getCurrentWeather() = requestService(getApiService().getCurrentWeather()) {
        val weatherList = it.weather

        val weatherValue = weatherList.firstOrNull()?.id ?: 0
        val temperature = it.main.temp
        val wearInfo = Util.recommendDress(temperature.roundToInt())
        val humidityValue = it.main.humidity
        val windValue = it.wind.speed

        val weatherInfo = WeatherInfo(
            Util.translateWeather(weatherValue),
            temperature,
            wearInfo,
            humidityValue,
            windValue
        )
        weatherInfoData.postValue(weatherInfo)
    }

}