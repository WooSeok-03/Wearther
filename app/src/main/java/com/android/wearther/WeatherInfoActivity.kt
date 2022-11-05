package com.android.wearther

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.wearther.data.model.forecast.ForecastInfo
import com.android.wearther.databinding.ActivityWeatherInfoBinding
import com.bumptech.glide.Glide

class WeatherInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityWeatherInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherInfoBinding.inflate(layoutInflater)

        showWeatherInfo()

        setContentView(binding.root)
    }

    private fun showWeatherInfo() {

        val forecastInfo = intent.getSerializableExtra("forecast_info") as ForecastInfo
        val recommend = "${forecastInfo.date} 에는\n${forecastInfo.wear} \n등을(를) 입는 것을 추천드립니다."

        binding.tvDate.text = forecastInfo.date
        binding.tvWeather.text = forecastInfo.weather.toString()
        binding.tvTemperature.text = getString(R.string.temperature_value, forecastInfo.temperature)
        binding.tvRecommend.text = recommend
        Glide.with(binding.ivWeather)
            .load(forecastInfo.icon)
            .into(binding.ivWeather)
    }

}


