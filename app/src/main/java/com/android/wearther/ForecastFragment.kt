package com.android.wearther

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.wearther.MainActivity
import com.android.wearther.R
import com.android.wearther.databinding.FragmentForecastBinding
import com.android.wearther.presentation.adapter.ForecastAdapter
import com.android.wearther.presentation.viewmodel.ForecastViewModel

class ForecastFragment : Fragment() {
    lateinit var binding: FragmentForecastBinding
    lateinit var viewModel: ForecastViewModel
    lateinit var forecastAdapter: ForecastAdapter

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

        initRecyclerView()
    }

    private fun initRecyclerView() {
        forecastAdapter = ForecastAdapter()
        binding.recyclerview.apply {
            adapter = forecastAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel.getForecastWeather()
        viewModel.forecast.observe(viewLifecycleOwner) { forecastAdapter.setList(it) }
    }


}

