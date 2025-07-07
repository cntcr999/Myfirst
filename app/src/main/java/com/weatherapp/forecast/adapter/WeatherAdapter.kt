package com.weatherapp.forecast.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.weatherapp.forecast.R
import com.weatherapp.forecast.databinding.ItemWeatherForecastBinding
import com.weatherapp.forecast.model.WeatherData
import java.text.SimpleDateFormat
import java.util.*

class WeatherAdapter(
    private val onItemClick: (WeatherData) -> Unit
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    
    private var forecastList = mutableListOf<WeatherData>()
    
    fun updateForecast(newForecast: List<WeatherData>) {
        forecastList.clear()
        forecastList.addAll(newForecast)
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemWeatherForecastBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WeatherViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(forecastList[position])
    }
    
    override fun getItemCount(): Int = forecastList.size
    
    inner class WeatherViewHolder(
        private val binding: ItemWeatherForecastBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(weatherData: WeatherData) {
            binding.apply {
                // Format date
                val dateFormat = if (weatherData.date.isNotEmpty()) {
                    try {
                        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        val outputFormat = SimpleDateFormat("MM/dd EEEE", Locale.getDefault())
                        val date = inputFormat.parse(weatherData.date)
                        date?.let { outputFormat.format(it) } ?: "今天"
                    } catch (e: Exception) {
                        "今天"
                    }
                } else {
                    "今天"
                }
                
                tvForecastDate.text = dateFormat
                tvForecastTemp.text = "${weatherData.temperature.toInt()}°C"
                tvForecastDescription.text = weatherData.description
                tvForecastHumidity.text = "濕度 ${weatherData.humidity}%"
                
                // Set weather icon
                when {
                    weatherData.description.contains("晴") || weatherData.description.contains("sunny") -> {
                        ivForecastIcon.setImageResource(R.drawable.ic_sunny)
                    }
                    weatherData.description.contains("雨") || weatherData.description.contains("rain") -> {
                        ivForecastIcon.setImageResource(R.drawable.ic_rainy)
                    }
                    weatherData.description.contains("雲") || weatherData.description.contains("cloud") -> {
                        ivForecastIcon.setImageResource(R.drawable.ic_cloudy)
                    }
                    weatherData.description.contains("雪") || weatherData.description.contains("snow") -> {
                        ivForecastIcon.setImageResource(R.drawable.ic_snowy)
                    }
                    else -> {
                        ivForecastIcon.setImageResource(R.drawable.ic_default_weather)
                    }
                }
                
                root.setOnClickListener {
                    onItemClick(weatherData)
                }
            }
        }
    }
}