package com.android.wearther

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.wearther.databinding.ActivityMainBinding
import com.android.wearther.presentation.adapter.ForecastAdapter
import com.android.wearther.presentation.viewmodel.ForecastViewModel
import com.android.wearther.presentation.viewmodel.WeatherViewModel
import java.text.DateFormat
import java.util.Calendar

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

        startAlarm()
    }

    private fun startAlarm() {
        val alarmManager: AlarmManager = getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        val morningTime = Calendar.getInstance()
        morningTime.set(Calendar.HOUR_OF_DAY, 8)
        morningTime.set(Calendar.MINUTE, 0)

        val time = DateFormat.getTimeInstance(DateFormat.SHORT).format(morningTime.time)
        intent.putExtra("time", time)

        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)
        if (morningTime.before(Calendar.getInstance())) {   // 설정된 시간이 현재시간보다 이전인 경우, +1일
            morningTime.add(Calendar.DATE, 1)
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, morningTime.timeInMillis, pendingIntent)
    }

    private fun cancelAlarm() {
        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
    }

}