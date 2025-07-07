package com.weatherapp.forecast

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.weatherapp.forecast.databinding.ActivityMainBinding
import com.weatherapp.forecast.model.WeatherData
import com.weatherapp.forecast.adapter.WeatherAdapter
import com.weatherapp.forecast.viewmodel.WeatherViewModel
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WeatherViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var weatherAdapter: WeatherAdapter
    
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViewModel()
        setupUI()
        setupLocation()
        requestLocationPermission()
    }
    
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        
        viewModel.weatherData.observe(this) { weatherData ->
            weatherData?.let { updateUI(it) }
        }
        
        viewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.swipeRefreshLayout.isRefreshing = isLoading
        }
        
        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
        
        viewModel.forecastData.observe(this) { forecastList ->
            forecastList?.let {
                weatherAdapter.updateForecast(it)
            }
        }
    }
    
    private fun setupUI() {
        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "天氣預測"
        
        // Setup RecyclerView for forecast
        weatherAdapter = WeatherAdapter { weatherItem ->
            val intent = Intent(this, WeatherDetailActivity::class.java)
            intent.putExtra("weather_data", weatherItem)
            startActivity(intent)
        }
        
        binding.recyclerViewForecast.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = weatherAdapter
        }
        
        // Setup swipe refresh
        binding.swipeRefreshLayout.setOnRefreshListener {
            getCurrentLocationAndFetchWeather()
        }
        
        // Setup search button
        binding.btnSearch.setOnClickListener {
            val city = binding.etCityName.text.toString().trim()
            if (city.isNotEmpty()) {
                viewModel.getWeatherByCity(city)
                viewModel.getForecastByCity(city)
            } else {
                Toast.makeText(this, "請輸入城市名稱", Toast.LENGTH_SHORT).show()
            }
        }
        
        // Setup location button
        binding.btnCurrentLocation.setOnClickListener {
            getCurrentLocationAndFetchWeather()
        }
    }
    
    private fun setupLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }
    
    private fun requestLocationPermission() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            getCurrentLocationAndFetchWeather()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "需要位置權限來獲取當前位置的天氣",
                LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }
    
    private fun getCurrentLocationAndFetchWeather() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }
        
        binding.progressBar.visibility = View.VISIBLE
        
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                viewModel.getWeatherByLocation(it.latitude, it.longitude)
                viewModel.getForecastByLocation(it.latitude, it.longitude)
            } ?: run {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "無法獲取位置信息", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this, "位置獲取失敗: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun updateUI(weatherData: WeatherData) {
        binding.apply {
            tvCityName.text = weatherData.cityName
            tvTemperature.text = "${weatherData.temperature.toInt()}°C"
            tvDescription.text = weatherData.description
            tvHumidity.text = "濕度: ${weatherData.humidity}%"
            tvWindSpeed.text = "風速: ${weatherData.windSpeed} m/s"
            tvPressure.text = "氣壓: ${weatherData.pressure} hPa"
            tvFeelsLike.text = "體感溫度: ${weatherData.feelsLike.toInt()}°C"
            
            // Set weather icon based on condition
            when {
                weatherData.description.contains("晴") || weatherData.description.contains("sunny") -> {
                    ivWeatherIcon.setImageResource(R.drawable.ic_sunny)
                }
                weatherData.description.contains("雨") || weatherData.description.contains("rain") -> {
                    ivWeatherIcon.setImageResource(R.drawable.ic_rainy)
                }
                weatherData.description.contains("雲") || weatherData.description.contains("cloud") -> {
                    ivWeatherIcon.setImageResource(R.drawable.ic_cloudy)
                }
                weatherData.description.contains("雪") || weatherData.description.contains("snow") -> {
                    ivWeatherIcon.setImageResource(R.drawable.ic_snowy)
                }
                else -> {
                    ivWeatherIcon.setImageResource(R.drawable.ic_default_weather)
                }
            }
            
            weatherCardView.visibility = View.VISIBLE
        }
    }
    
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            getCurrentLocationAndFetchWeather()
        }
    }
    
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(this, "需要位置權限來獲取天氣信息", Toast.LENGTH_LONG).show()
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}