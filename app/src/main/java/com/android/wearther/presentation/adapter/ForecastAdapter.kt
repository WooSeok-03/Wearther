package com.android.wearther.presentation.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.wearther.WeatherInfoActivity
import com.android.wearther.data.model.week.Week
import com.android.wearther.databinding.ListItemBinding
import kotlin.math.roundToInt

class ForecastAdapter: RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {
    inner class ForecastViewHolder(private val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(week: Week) {
            val date = week.dt_txt.replace(" 12:00:00", "")
            binding.tvDate.text = date

            val weather = week.weather.first().description
            binding.tvWeather.text = translateWeather(weather)

            val temperature = week.main.temp.roundToInt().toString() + " ℃"
            binding.tvTemperature.text = temperature

            binding.cardView.setOnClickListener{
                val intent = Intent(it.context, WeatherInfoActivity::class.java)
                intent.putExtra("date", date)
                intent.putExtra("weather", weather)
                //intent.putExtra("temperature", week.main.temp.roundToInt())
                intent.putExtra("temperature", temperature)
                ContextCompat.startActivity(it.context, intent, null)
            }
        }
    }

    private val items = ArrayList<Week>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setList(weekList: List<Week>){
        items.clear()
        items.addAll(weekList)
        notifyDataSetChanged()
    }

    fun translateWeather(weather: String): String {
        return when(weather) {
            "clear sky" -> "맑음"
            "few clouds" -> "구름조금"
            "scattered clouds" -> "구름많음"
            "broken clouds" -> "흐림"
            "shower rain" -> "소나기"
            "rain" -> "비"
            "thunderstorm" -> "뇌우"
            "snow" -> "눈"
            "mist" -> "안개"
            else -> ""
        }
    }
}