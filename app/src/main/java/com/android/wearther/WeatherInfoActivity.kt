package com.android.wearther

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.wearther.databinding.ActivityWeatherInfoBinding
import com.bumptech.glide.Glide
import kotlin.reflect.typeOf

class WeatherInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityWeatherInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showWeatherInfo()
    }

    private fun showWeatherInfo() {
        val date = intent.getStringExtra("date")
        val weather = intent.getStringExtra("weather")
        val temperature = intent.getStringExtra("temperature")
        val recommend = date + "에는\n" + recommendDress(temperature ?: "") + "\n등을(를) 입는 것을 추천드립니다."
        val icon = intent.getIntExtra("icon", R.drawable.icon_sunny)

        binding.tvDate.text = date
        binding.tvWeather.text = weather
        binding.tvTemperature.text = temperature
        binding.tvRecommend.text = recommend
        Glide.with(binding.ivWeather)
            .load(icon)
            .into(binding.ivWeather)
    }

    private fun recommendDress(temperatureValue: String) : String {
        lateinit var clothes: String
        val temperature = temperatureValue?.replace(" ℃", "")?.toInt() ?: 0

        if(temperature >= 28) {               // 28℃ ~
            clothes = "민소매, 반바지, 반팔, \n치마, 원피스"
        } else if (temperature in 23..27) { // 23℃ ~ 27℃
            clothes = "반팔, 얇은 셔츠, \n반바지, 면바지"
        } else if (temperature in 20..22) { // 20℃ ~ 22℃
            clothes = "긴팔, 얇은 가디건, \n면바지, 청바지"
        } else if (temperature in 17..19) { // 17℃ ~ 19℃
            clothes = "얇은 니트, 얇은 재킷, \n가디건, 맨투맨, 면바지, 청바지"
        } else if (temperature in 12..16) { // 12℃ ~ 16℃
            clothes = "얇은 재킷, 가디건, 야상, 맨투맨, \n니트, 청바지, 면바지, 살구색 스타킹"
        } else if (temperature in 9..11) {  // 9℃ ~ 11℃
            clothes = "재킷, 트랜치 코트, 야상, \n니트, 면바지, 청바지, 검은색 스타킹"
        } else if (temperature in 5..8) {   // 5℃ ~ 8℃
            clothes = "코트, 가죽 재킷, 니트, \n스카프, 두꺼운 바지, 히트텍"
        } else {                                // ~ 4℃
            clothes = "패딩, 두꺼운 코트, \n목도리, 기모 제품"
        }

        return clothes
    }
}


