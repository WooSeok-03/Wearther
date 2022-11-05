package com.android.wearther

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.android.wearther.data.model.WeatherConditions
import com.android.wearther.data.model.current.WeatherInfo
import com.android.wearther.databinding.FragmentWeatherBinding
import com.android.wearther.presentation.Util
import com.android.wearther.presentation.viewmodel.WeatherViewModel
import com.bumptech.glide.Glide
import java.util.*

class WeatherFragment : Fragment() {
    lateinit var binding: FragmentWeatherBinding
    lateinit var viewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeatherBinding.bind(view)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        showCurrentWeather()
    }

    private fun showCurrentWeather() {
        viewModel.getCurrentWeather()

        viewModel.weatherInfo.observe(viewLifecycleOwner) {
            binding.tvWeather.text = it.weather.toString()
            binding.tvTemperature.text = getString(R.string.temperature_value, it.temperature)
            binding.tvHumidity.text = getString(R.string.humidity_value, it.humidity)
            binding.tvWind.text = getString(R.string.wind_value, it.wind)
            binding.tvWear.text = it.wear

            val resourceId = Util.getIconFromWeather(it.weather)

            Glide.with(this).load(resourceId).into(binding.ivWeather)
            binding.weatherImages.visibility = View.VISIBLE

            morningNotification(it)
        }
    }

    private fun morningNotification(weatherInfo: WeatherInfo) {
        val application = requireActivity().application

        val alarmManager: AlarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(application, AlarmReceiver::class.java)

        val morningTime = Calendar.getInstance()
        morningTime.set(Calendar.HOUR_OF_DAY, 8)
        morningTime.set(Calendar.MINUTE, 0)

        intent.putExtra("weather", weatherInfo.weather)
        intent.putExtra("temperature", weatherInfo.temperature)
        intent.putExtra("wear", weatherInfo.wear)


        val pendingIntent = PendingIntent.getBroadcast(application, 1, intent, PendingIntent.FLAG_IMMUTABLE)
        if (morningTime.before(Calendar.getInstance())) {   // 설정된 시간이 현재시간보다 이전인 경우, +1일
            morningTime.add(Calendar.DATE, 1)
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, morningTime.timeInMillis, pendingIntent)
    }


}