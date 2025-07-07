package com.weatherapp.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherapp.forecast.model.WeatherData
import com.weatherapp.forecast.network.WeatherApiService
import com.weatherapp.forecast.network.RetrofitInstance
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class WeatherViewModel : ViewModel() {
    
    private val apiService = RetrofitInstance.api
    
    private val _weatherData = MutableLiveData<WeatherData?>()
    val weatherData: LiveData<WeatherData?> = _weatherData
    
    private val _forecastData = MutableLiveData<List<WeatherData>?>()
    val forecastData: LiveData<List<WeatherData>?> = _forecastData
    
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    fun getWeatherByCity(cityName: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            try {
                val response = apiService.getCurrentWeather(
                    cityName = cityName,
                    apiKey = "YOUR_API_KEY", // 需要在OpenWeatherMap註冊獲取API Key
                    units = "metric",
                    lang = "zh_tw"
                )
                
                if (response.isSuccessful) {
                    response.body()?.let { weatherResponse ->
                        val weatherData = WeatherData(
                            cityName = weatherResponse.name,
                            temperature = weatherResponse.main.temp,
                            description = weatherResponse.weather[0].description,
                            humidity = weatherResponse.main.humidity,
                            windSpeed = weatherResponse.wind.speed,
                            pressure = weatherResponse.main.pressure,
                            feelsLike = weatherResponse.main.feels_like,
                            visibility = weatherResponse.visibility / 1000.0, // Convert to km
                            uvIndex = 5.0, // Mock value
                            sunrise = weatherResponse.sys.sunrise,
                            sunset = weatherResponse.sys.sunset,
                            icon = weatherResponse.weather[0].icon
                        )
                        _weatherData.value = weatherData
                    }
                } else {
                    _error.value = "無法獲取天氣數據: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "網路錯誤: ${e.message}"
                // 使用模擬數據作為備用
                _weatherData.value = getMockWeatherData(cityName)
            } finally {
                _loading.value = false
            }
        }
    }
    
    fun getWeatherByLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            try {
                val response = apiService.getCurrentWeatherByCoordinates(
                    lat = latitude,
                    lon = longitude,
                    apiKey = "YOUR_API_KEY", // 需要在OpenWeatherMap註冊獲取API Key
                    units = "metric",
                    lang = "zh_tw"
                )
                
                if (response.isSuccessful) {
                    response.body()?.let { weatherResponse ->
                        val weatherData = WeatherData(
                            cityName = weatherResponse.name,
                            temperature = weatherResponse.main.temp,
                            description = weatherResponse.weather[0].description,
                            humidity = weatherResponse.main.humidity,
                            windSpeed = weatherResponse.wind.speed,
                            pressure = weatherResponse.main.pressure,
                            feelsLike = weatherResponse.main.feels_like,
                            visibility = weatherResponse.visibility / 1000.0,
                            uvIndex = 5.0,
                            sunrise = weatherResponse.sys.sunrise,
                            sunset = weatherResponse.sys.sunset,
                            icon = weatherResponse.weather[0].icon
                        )
                        _weatherData.value = weatherData
                    }
                } else {
                    _error.value = "無法獲取天氣數據: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "網路錯誤: ${e.message}"
                // 使用模擬數據作為備用
                _weatherData.value = getMockWeatherData("當前位置")
            } finally {
                _loading.value = false
            }
        }
    }
    
    fun getForecastByCity(cityName: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getForecast(
                    cityName = cityName,
                    apiKey = "YOUR_API_KEY",
                    units = "metric",
                    lang = "zh_tw"
                )
                
                if (response.isSuccessful) {
                    response.body()?.let { forecastResponse ->
                        val forecastList = forecastResponse.list.take(5).map { forecast ->
                            WeatherData(
                                cityName = forecastResponse.city.name,
                                temperature = forecast.main.temp,
                                description = forecast.weather[0].description,
                                humidity = forecast.main.humidity,
                                windSpeed = forecast.wind.speed,
                                pressure = forecast.main.pressure,
                                feelsLike = forecast.main.feels_like,
                                date = forecast.dt_txt,
                                icon = forecast.weather[0].icon
                            )
                        }
                        _forecastData.value = forecastList
                    }
                }
            } catch (e: Exception) {
                // 使用模擬預測數據
                _forecastData.value = getMockForecastData()
            }
        }
    }
    
    fun getForecastByLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val response = apiService.getForecastByCoordinates(
                    lat = latitude,
                    lon = longitude,
                    apiKey = "YOUR_API_KEY",
                    units = "metric",
                    lang = "zh_tw"
                )
                
                if (response.isSuccessful) {
                    response.body()?.let { forecastResponse ->
                        val forecastList = forecastResponse.list.take(5).map { forecast ->
                            WeatherData(
                                cityName = forecastResponse.city.name,
                                temperature = forecast.main.temp,
                                description = forecast.weather[0].description,
                                humidity = forecast.main.humidity,
                                windSpeed = forecast.wind.speed,
                                pressure = forecast.main.pressure,
                                feelsLike = forecast.main.feels_like,
                                date = forecast.dt_txt,
                                icon = forecast.weather[0].icon
                            )
                        }
                        _forecastData.value = forecastList
                    }
                }
            } catch (e: Exception) {
                // 使用模擬預測數據
                _forecastData.value = getMockForecastData()
            }
        }
    }
    
    private fun getMockWeatherData(cityName: String): WeatherData {
        return WeatherData(
            cityName = cityName,
            temperature = 25.0,
            description = "多雲",
            humidity = 65,
            windSpeed = 3.5,
            pressure = 1013.0,
            feelsLike = 27.0,
            visibility = 10.0,
            uvIndex = 6.0,
            sunrise = System.currentTimeMillis() / 1000 - 3600 * 2, // 2 hours ago
            sunset = System.currentTimeMillis() / 1000 + 3600 * 6   // 6 hours later
        )
    }
    
    private fun getMockForecastData(): List<WeatherData> {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        
        return (1..5).map { day ->
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            WeatherData(
                cityName = "預測",
                temperature = (20..30).random().toDouble(),
                description = listOf("晴天", "多雲", "小雨", "陰天").random(),
                humidity = (50..80).random(),
                windSpeed = (2.0..8.0).random(),
                pressure = (1000..1020).random().toDouble(),
                feelsLike = (22..32).random().toDouble(),
                date = sdf.format(calendar.time)
            )
        }
    }
}