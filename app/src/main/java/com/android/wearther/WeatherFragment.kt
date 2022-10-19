package com.android.wearther

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import com.android.wearther.databinding.FragmentWeatherBinding
import com.android.wearther.presentation.viewmodel.WeatherViewModel
import com.android.wearther.presentation.viewmodel.WeatherViewModelFactory
import com.bumptech.glide.Glide
import java.text.DateFormat
import java.util.*

class WeatherFragment : Fragment() {
    lateinit var binding: FragmentWeatherBinding
    lateinit var viewModel: WeatherViewModel
    lateinit var factory: WeatherViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeatherBinding.bind(view)

        //viewModel = (activity as MainActivity).weatherViewModel
        val application = requireActivity().application
        factory = WeatherViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(WeatherViewModel::class.java)

        showCurrentWeather()
        morningNotification()
    }

    private fun showCurrentWeather() {
        viewModel.getCurrentWeather()

        viewModel.weather.observe(viewLifecycleOwner) { binding.tvWeather.text = it }
        viewModel.wear.observe(viewLifecycleOwner) { binding.tvWear.text = it }

        viewModel.temperature.observe(viewLifecycleOwner) {
            binding.tvTemperature.text = it
            Glide.with(binding.ivTemperature)
                .load(R.drawable.icon_temperature)
                .into(binding.ivTemperature)
        }

        viewModel.humidity.observe(viewLifecycleOwner) {
            binding.tvHumidity.text = it
            Glide.with(binding.ivHumidity)
                .load(R.drawable.icon_humidity)
                .into(binding.ivHumidity)
        }

        viewModel.wind.observe(viewLifecycleOwner) {
            binding.tvWind.text = it
            Glide.with(binding.ivWind)
                .load(R.drawable.icon_wind)
                .into(binding.ivWind)
        }

        viewModel.weatherIcon.observe(viewLifecycleOwner) {
            Glide.with(binding.ivWeather)
                .load(it)
                .into(binding.ivWeather)
        }
    }

    private fun morningNotification() {
        viewModel.notificationData.observe(viewLifecycleOwner) {
            viewModel.startAlarm()   // Notification
        }
    }

}