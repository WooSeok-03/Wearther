package com.android.wearther.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.wearther.R
import com.android.wearther.WeatherInfoActivity
import com.android.wearther.data.model.week.ForecastInfo
import com.android.wearther.databinding.ListItemBinding
import com.bumptech.glide.Glide
import java.util.*

class ForecastAdapter: RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {
    inner class ForecastViewHolder(private val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(forecastInfo: ForecastInfo) {

            binding.tvDate.text = forecastInfo.date
            binding.tvWeather.text = forecastInfo.weather
            binding.tvTemperature.text = itemView.context.getString(
                R.string.temperature_value,
                forecastInfo.temperature
            )

            Glide.with(binding.ivWeather)
                .load(forecastInfo.icon)
                .into(binding.ivWeather)

            binding.ivTemperature.visibility = View.VISIBLE

            binding.cardView.setOnClickListener{
                val intent = Intent(it.context, WeatherInfoActivity::class.java)
                intent.putExtra("forecast_info", forecastInfo)
                ContextCompat.startActivity(it.context, intent, null)
            }
        }
    }

    private val callback = object : DiffUtil.ItemCallback<ForecastInfo>() {
        override fun areItemsTheSame(oldItem: ForecastInfo, newItem: ForecastInfo): Boolean =
            oldItem.date == newItem.date

        override fun areContentsTheSame(oldItem: ForecastInfo, newItem: ForecastInfo): Boolean =
            oldItem == newItem
    }
    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size
}