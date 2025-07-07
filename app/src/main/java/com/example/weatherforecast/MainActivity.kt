package com.example.weatherforecast

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.adapter.ForecastAdapter
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.network.WeatherApi
import com.example.weatherforecast.network.WeatherService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var forecastAdapter: ForecastAdapter
    private val apiKey = "YOUR_API_KEY_HERE" // 請替換為您的OpenWeatherMap API密鑰

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        
        setupRecyclerView()
        setupRefreshListener()
        checkPermissionsAndGetLocation()
    }

    private fun setupRecyclerView() {
        forecastAdapter = ForecastAdapter()
        binding.recyclerViewForecast.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = forecastAdapter
        }
    }

    private fun setupRefreshListener() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            checkPermissionsAndGetLocation()
        }
    }

    private fun checkPermissionsAndGetLocation() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report?.areAllPermissionsGranted() == true) {
                        getCurrentLocation()
                    } else {
                        Toast.makeText(this@MainActivity, "需要位置權限來獲取天氣信息", Toast.LENGTH_SHORT).show()
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .check()
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    getWeatherData(it.latitude, it.longitude)
                } ?: run {
                    Toast.makeText(this, "無法獲取位置", Toast.LENGTH_SHORT).show()
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "位置獲取失敗", Toast.LENGTH_SHORT).show()
                binding.swipeRefreshLayout.isRefreshing = false
            }
    }

    private fun getWeatherData(lat: Double, lon: Double) {
        showLoading(true)
        
        val weatherService = WeatherService.create()
        
        // 獲取當前天氣
        weatherService.getCurrentWeather(lat, lon, apiKey, "metric", "zh_tw")
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { weatherData ->
                            updateCurrentWeatherUI(weatherData)
                            // 獲取5天預報
                            getForecastData(lat, lon)
                        }
                    } else {
                        showError("天氣數據獲取失敗")
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    showError("網絡連接錯誤: ${t.message}")
                }
            })
    }

    private fun getForecastData(lat: Double, lon: Double) {
        val weatherService = WeatherService.create()
        
        weatherService.getForecast(lat, lon, apiKey, "metric", "zh_tw")
            .enqueue(object : Callback<WeatherApi.ForecastResponse> {
                override fun onResponse(
                    call: Call<WeatherApi.ForecastResponse>,
                    response: Response<WeatherApi.ForecastResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { forecastData ->
                            updateForecastUI(forecastData)
                        }
                    }
                    showLoading(false)
                    binding.swipeRefreshLayout.isRefreshing = false
                }

                override fun onFailure(call: Call<WeatherApi.ForecastResponse>, t: Throwable) {
                    showLoading(false)
                    binding.swipeRefreshLayout.isRefreshing = false
                    showError("預報數據獲取失敗")
                }
            })
    }

    private fun updateCurrentWeatherUI(weather: WeatherResponse) {
        binding.apply {
            textViewLocation.text = weather.name
            textViewTemperature.text = "${weather.main.temp.roundToInt()}°"
            textViewDescription.text = weather.weather[0].description
            textViewFeelsLike.text = "體感 ${weather.main.feels_like.roundToInt()}°"
            textViewHumidity.text = "${weather.main.humidity}%"
            textViewWindSpeed.text = "${weather.wind.speed} m/s"
            textViewPressure.text = "${weather.main.pressure} hPa"
            textViewVisibility.text = "${weather.visibility / 1000} km"
            
            val date = SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault()).format(Date())
            textViewDate.text = date
        }
    }

    private fun updateForecastUI(forecast: WeatherApi.ForecastResponse) {
        // 過濾每天中午12點的數據作為日預報
        val dailyForecasts = forecast.list.filter { 
            it.dt_txt.contains("12:00:00") 
        }.take(5)
        
        forecastAdapter.updateData(dailyForecasts)
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.layoutContent.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        showLoading(false)
        binding.swipeRefreshLayout.isRefreshing = false
    }
}