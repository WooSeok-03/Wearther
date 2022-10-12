package com.android.wearther.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.wearther.data.model.week.Week
import com.android.wearther.databinding.ListItemBinding
import kotlin.math.roundToInt

class ForecastAdapter: RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {
    inner class ForecastViewHolder(private val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(week: Week) {
            binding.tvDate.text = week.dt_txt.replace(" 12:00:00", "")
            binding.tvWeather.text = week.weather.first().main

            //val temperature = week.main.temp_max.roundToInt().toString() + " ℃ / " + week.main.temp_min.roundToInt().toString() + " ℃"
            val temperature = week.main.temp.roundToInt().toString() + " ℃"
            binding.tvTemperature.text = temperature
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
}