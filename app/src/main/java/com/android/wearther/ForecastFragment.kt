package com.android.wearther.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.wearther.MainActivity
import com.android.wearther.R
import com.android.wearther.databinding.FragmentForecastBinding
import com.android.wearther.presentation.viewmodel.ForecastViewModel

class ForecastFragment : Fragment() {
    lateinit var binding: FragmentForecastBinding
    lateinit var viewModel: ForecastViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForecastBinding.bind(view)
        viewModel = (activity as MainActivity).forecastViewModel

        Log.i("MYTAG", "${viewModel.getForecastWeather()}")
    }

}