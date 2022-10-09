package com.android.wearther

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.wearther.databinding.ActivityMainBinding
import com.android.wearther.presentation.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        showTemperature()
    }

    private fun showTemperature() {
        viewModel.getCurrentWeather()
        viewModel.getWeekWeather()

        viewModel.temperature.observe(this, Observer {
            binding.temperature.text = it
        })
        viewModel.maxTemperature.observe(this, Observer {
            binding.maxTemperature.text = it
        })
        viewModel.minTemperature.observe(this, Observer {
            binding.minTemperature.text = it
        })
    }
}