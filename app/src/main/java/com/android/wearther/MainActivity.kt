package com.android.wearther

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.wearther.databinding.ActivityMainBinding
import com.android.wearther.viewmodel.ForecastViewModel
import com.android.wearther.viewmodel.WeatherViewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var weatherViewModel: WeatherViewModel
    lateinit var forecastViewModel: ForecastViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bnvMenu.setupWithNavController(navController)

        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        forecastViewModel = ViewModelProvider(this).get(ForecastViewModel::class.java)
    }


}