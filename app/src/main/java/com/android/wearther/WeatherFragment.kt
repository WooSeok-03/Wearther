package com.android.wearther

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.android.wearther.databinding.FragmentWeatherBinding
import com.android.wearther.presentation.WeatherViewModel

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

        showTemperature()
    }

    private fun showTemperature() {
        viewModel.getCurrentWeather()

        viewModel.weather.observe(viewLifecycleOwner) {
            binding.weather.text = it
        }
        viewModel.temperature.observe(viewLifecycleOwner, Observer {
            binding.temperature.text = it
        })
    }

}