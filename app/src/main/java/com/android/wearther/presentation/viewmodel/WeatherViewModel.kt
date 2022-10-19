package com.android.wearther.presentation.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.wearther.AlarmReceiver
import com.android.wearther.R
import com.android.wearther.data.api.RetrofitClient.Companion.getApiService
import com.android.wearther.data.model.current.Weather
import com.bumptech.glide.Glide
import retrofit2.*
import java.text.DateFormat
import java.util.*
import kotlin.math.roundToInt

class WeatherViewModel(private val application: Application): ViewModel(){

    var weatherIconData = MutableLiveData<Int>()
    val weatherIcon : LiveData<Int>
    get() = weatherIconData

    var temperatureData = MutableLiveData<String>()
    val temperature: LiveData<String>
    get() = temperatureData

    var weatherData = MutableLiveData<String>()
    val weather: LiveData<String>
    get() = weatherData

    var humidityData = MutableLiveData<String>()
    val humidity : LiveData<String>
    get() = humidityData

    var windData = MutableLiveData<String>()
    val wind : LiveData<String>
    get() = windData

    var wearData = MutableLiveData<String>()
    val wear : LiveData<String>
    get() = wearData

    val notificationData = MediatorLiveData<String>()
    init {
        notificationData.addSource(weather) { value ->
            notificationData.value = value
        }
        notificationData.addSource(temperature) { value ->
            notificationData.value = value
        }
        notificationData.addSource(wear) { value ->
            notificationData.value = value
        }
    }

    fun getCurrentWeather() {
        val service = getApiService()

        service.getCurrentWeather().enqueue(object : Callback<Weather>{
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                Log.i("MYTAG", "onResponse() : $response")
                if(response.code() == 200) {
                    Log.i("MYTAG", "response.body : ${response.body()}")

                    // Current Weather
                    val weatherList = response.body()?.weather
                    if (weatherList != null) {
                        Log.i("MYTAG", "$weatherList")
                        val weatherValue = weatherList.first().id
                        weatherData.postValue(translateWeather(weatherValue))
                    }

                    // Current Temperature
                    val temperatureValue: Double = response.body()?.main?.temp_max ?: 0.0
                    temperatureData.postValue(temperatureValue.roundToInt().toString() + "℃")

                    // Recommend Dress
                    wearData.postValue(recommendDress(temperatureValue.roundToInt()))

                    // Current Humidity
                    val humidityValue = response.body()?.main?.humidity
                    humidityData.postValue(humidityValue.toString() + "%")

                    // Current Wind
                    val windValue = response.body()?.wind?.speed
                    windData.postValue(windValue.toString() + "m/s")

                    val weatherIconValue = when(weather.value) {
                        "뇌우" -> R.drawable.icon_thunder
                        "이슬비" -> R.drawable.icon_drizzle
                        "비" -> R.drawable.icon_rain
                        "눈" -> R.drawable.icon_snow
                        "안개" -> R.drawable.icon_mist
                        "맑음" -> R.drawable.icon_sunny
                        "구름조금" -> R.drawable.icon_little_cloud
                        "구름많음" -> R.drawable.icon_many_cloud
                        "흐림" -> R.drawable.icon_cloudy
                        else -> R.drawable.icon_sunny
                    }
                    weatherIconData.postValue(weatherIconValue)


                } else {
                    Log.i("MYTAG", "Error : $response")
                }
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                Log.i("MYTAG", t.localizedMessage)
            }
        })
    }

    fun translateWeather(weather: Int): String {
        return when (weather) {
            in 200..299 -> "뇌우"
            in 300..499 -> "이슬비"
            in 500..599 -> "비"
            in 600..699 -> "눈"
            in 701..799 -> "안개"
            800 -> "맑음"
            801 -> "구름조금"
            802 -> "구름많음"
            in 803..804 -> "흐림"
            else -> ""
        }
    }

    fun recommendDress(currentTemperature: Int) : String {
        lateinit var clothes: String

        if(currentTemperature >= 27) {               // 27℃ ~
            clothes = "민소매, 반바지, 반팔, 치마, 원피스"
        } else if (currentTemperature in 23..26) { // 23℃ ~ 26℃
            clothes = "반팔, 얇은 셔츠, 반바지, 면바지"
        } else if (currentTemperature in 20..22) { // 20℃ ~ 22℃
            clothes = "긴팔, 얇은 가디건, 면바지, 청바지"
        } else if (currentTemperature in 17..19) { // 17℃ ~ 19℃
            clothes = "얇은 니트, 얇은 재킷, 가디건, 맨투맨, 면바지, 청바지"
        } else if (currentTemperature in 12..16) { // 12℃ ~ 16℃
            clothes = "얇은 재킷, 가디건, 야상, 맨투맨, 니트, 청바지, 면바지, 살구색 스타킹"
        } else if (currentTemperature in 9..11) {  // 9℃ ~ 11℃
            clothes = "재킷, 트랜치 코트, 야상, 니트, 면바지, 청바지, 검은색 스타킹"
        } else if (currentTemperature in 5..8) {   // 5℃ ~ 8℃
            clothes = "코트, 가죽 재킷, 니트, 스카프, 두꺼운 바지, 히트텍"
        } else {                                // ~ 4℃
            clothes = "패딩, 두꺼운 코트, 목도리, 기모 제품"
        }

        return clothes
    }


    fun startAlarm() {
        if (weather.value == null || temperature.value == null || wear.value == null) return

        val alarmManager: AlarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(application, AlarmReceiver::class.java)

        val morningTime = Calendar.getInstance()
        morningTime.set(Calendar.HOUR_OF_DAY, 8)
        morningTime.set(Calendar.MINUTE, 0)

        Log.i("MYTAG", "start: ${weather.value} / ${temperature.value} / ${wear.value}")
        intent.putExtra("weather", weather.value)
        intent.putExtra("temperature", temperature.value)
        intent.putExtra("wear", wear.value)


        val pendingIntent = PendingIntent.getBroadcast(application, 1, intent, PendingIntent.FLAG_IMMUTABLE)
        if (morningTime.before(Calendar.getInstance())) {   // 설정된 시간이 현재시간보다 이전인 경우, +1일
            morningTime.add(Calendar.DATE, 1)
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, morningTime.timeInMillis, pendingIntent)
    }

    private fun cancelAlarm(application: Application) {
        val alarmManager: AlarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(application, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(application, 1, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
    }

}

