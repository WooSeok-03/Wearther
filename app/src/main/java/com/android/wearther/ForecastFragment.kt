package com.android.wearther

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.wearther.MainActivity
import com.android.wearther.R
import com.android.wearther.databinding.FragmentForecastBinding
import com.android.wearther.presentation.adapter.ForecastAdapter
import com.android.wearther.presentation.viewmodel.ForecastViewModel
import com.android.wearther.presentation.viewmodel.WeatherViewModel

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
        viewModel = ViewModelProvider(this).get(ForecastViewModel::class.java)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        forecastAdapter = ForecastAdapter().apply {
            setOnCardClickListener {
                val intent = Intent(requireContext(), WeatherInfoActivity::class.java)
                intent.putExtra("forecast_info", it)
                startActivity(intent)
            }
        }
        binding.recyclerview.apply {
            adapter = forecastAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel.getForecastWeather()
        viewModel.forecastInfo.observe(viewLifecycleOwner) { forecastAdapter.differ.submitList(it) }
    }


}

