package com.example.weatherforecast.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.network.WeatherApi
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {
    
    private var forecastList = listOf<WeatherApi.ForecastItem>()
    
    fun updateData(newList: List<WeatherApi.ForecastItem>) {
        forecastList = newList
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forecast, parent, false)
        return ForecastViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(forecastList[position])
    }
    
    override fun getItemCount(): Int = forecastList.size
    
    class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewDay: TextView = itemView.findViewById(R.id.textViewDay)
        private val imageViewWeather: ImageView = itemView.findViewById(R.id.imageViewWeather)
        private val textViewHighTemp: TextView = itemView.findViewById(R.id.textViewHighTemp)
        private val textViewLowTemp: TextView = itemView.findViewById(R.id.textViewLowTemp)
        private val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        
        fun bind(forecast: WeatherApi.ForecastItem) {
            // 格式化日期
            val date = Date(forecast.dt * 1000)
            val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
            textViewDay.text = dayFormat.format(date)
            
            // 設置溫度
            textViewHighTemp.text = "${forecast.main.temp_max.roundToInt()}°"
            textViewLowTemp.text = "${forecast.main.temp_min.roundToInt()}°"
            
            // 設置天氣描述
            textViewDescription.text = forecast.weather[0].description
            
            // 設置天氣圖標（這裡使用簡單的文字替代）
            val iconResource = when(forecast.weather[0].main.lowercase()) {
                "clear" -> "☀️"
                "clouds" -> "☁️"
                "rain" -> "🌧️"
                "drizzle" -> "🌦️"
                "thunderstorm" -> "⛈️"
                "snow" -> "❄️"
                "mist", "fog" -> "🌫️"
                else -> "🌤️"
            }
            
            // 由於我們沒有實際的ImageView設置，我們可以將圖標作為文字顯示
            // 在實際應用中，您會使用Glide或Picasso來加載天氣圖標
        }
    }
}