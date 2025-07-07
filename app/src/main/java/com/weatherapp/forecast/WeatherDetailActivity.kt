package com.weatherapp.forecast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.weatherapp.forecast.databinding.ActivityWeatherDetailBinding
import com.weatherapp.forecast.model.WeatherData
import java.text.SimpleDateFormat
import java.util.*

class WeatherDetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityWeatherDetailBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        
        val weatherData = intent.getParcelableExtra<WeatherData>("weather_data")
        weatherData?.let { displayWeatherDetails(it) }
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "天氣詳情"
        
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    
    private fun displayWeatherDetails(weatherData: WeatherData) {
        binding.apply {
            // Main weather info
            tvDetailCityName.text = weatherData.cityName
            tvDetailTemperature.text = "${weatherData.temperature.toInt()}°C"
            tvDetailDescription.text = weatherData.description
            
            // Additional details
            tvDetailHumidity.text = "${weatherData.humidity}%"
            tvDetailWindSpeed.text = "${weatherData.windSpeed} m/s"
            tvDetailPressure.text = "${weatherData.pressure} hPa"
            tvDetailFeelsLike.text = "${weatherData.feelsLike.toInt()}°C"
            tvDetailVisibility.text = "${weatherData.visibility} km"
            tvDetailUvIndex.text = "${weatherData.uvIndex}"
            
            // Sun times
            tvDetailSunrise.text = formatTime(weatherData.sunrise)
            tvDetailSunset.text = formatTime(weatherData.sunset)
            
            // Date
            tvDetailDate.text = SimpleDateFormat("yyyy年MM月dd日 EEEE", Locale.getDefault()).format(Date())
            
            // Set weather icon
            when {
                weatherData.description.contains("晴") || weatherData.description.contains("sunny") -> {
                    ivDetailWeatherIcon.setImageResource(R.drawable.ic_sunny)
                }
                weatherData.description.contains("雨") || weatherData.description.contains("rain") -> {
                    ivDetailWeatherIcon.setImageResource(R.drawable.ic_rainy)
                }
                weatherData.description.contains("雲") || weatherData.description.contains("cloud") -> {
                    ivDetailWeatherIcon.setImageResource(R.drawable.ic_cloudy)
                }
                weatherData.description.contains("雪") || weatherData.description.contains("snow") -> {
                    ivDetailWeatherIcon.setImageResource(R.drawable.ic_snowy)
                }
                else -> {
                    ivDetailWeatherIcon.setImageResource(R.drawable.ic_default_weather)
                }
            }
        }
    }
    
    private fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }
}