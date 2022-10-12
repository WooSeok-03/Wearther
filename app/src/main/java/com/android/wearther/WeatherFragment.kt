package com.android.wearther

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.android.wearther.MainActivity
import com.android.wearther.R
import com.android.wearther.databinding.FragmentWeatherBinding
import com.android.wearther.presentation.viewmodel.WeatherViewModel

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
        viewModel = (activity as MainActivity).weatherViewModel

        showCurrentWeather()
    }

    private fun showCurrentWeather() {
        viewModel.getCurrentWeather()

        viewModel.weather.observe(viewLifecycleOwner) { binding.tvWeather.text = it }
        viewModel.temperature.observe(viewLifecycleOwner) { binding.tvTemperature.text = it }
        viewModel.humidity.observe(viewLifecycleOwner) { binding.tvHumidity.text = it }
        viewModel.wind.observe(viewLifecycleOwner) { binding.tvWind.text = it }
    }

}