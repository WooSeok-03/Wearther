package com.android.wearther.presentation.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.wearther.R
import com.android.wearther.data.api.RetrofitClient.Companion.getApiService
import com.android.wearther.data.model.forecast.WeekWeather
import com.android.wearther.data.model.forecast.ForecastInfo
import com.android.wearther.data.model.forecast.Week
import com.android.wearther.presentation.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class ForecastViewModel : BaseViewModel() {

    private val forecastInfoData = MutableLiveData<List<ForecastInfo>>()
    val forecastInfo: LiveData<List<ForecastInfo>>
        get() = forecastInfoData

    fun getForecastWeather() = requestService(getApiService().getForecastWeather()) { weekList ->
        val forecastList = weekList.list
            .filter { it.dt_txt.contains("12:00:00") }
            .mapNotNull(this@ForecastViewModel::weekToForecastInfo)

        forecastInfoData.postValue(forecastList)
    }

    private fun weekToForecastInfo(date: Week): ForecastInfo? {
        val weatherValue = date.weather.firstOrNull()?.id ?: return null
        val dateValue = date.dt_txt.replace(" 12:00:00", "")
        val temperature = date.main.temp
        val wearInfo = Util.recommendDress(temperature.roundToInt())
        val weatherConditions = Util.translateWeather(weatherValue)

        val weatherIcon = Util.getIconFromWeather(weatherConditions)
        return ForecastInfo(
            dateValue,
            weatherConditions,
            temperature,
            wearInfo,
            weatherIcon
        )
    }
}

