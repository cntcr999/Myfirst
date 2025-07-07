package com.example.weatherforecast.network

import com.example.weatherforecast.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    
    @GET("weather")
    fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "zh_tw"
    ): Call<WeatherResponse>
    
    @GET("forecast")
    fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "zh_tw"
    ): Call<ForecastResponse>
    
    data class ForecastResponse(
        val cod: String,
        val message: Int,
        val cnt: Int,
        val list: List<ForecastItem>,
        val city: City
    )
    
    data class ForecastItem(
        val dt: Long,
        val main: com.example.weatherforecast.model.Main,
        val weather: List<com.example.weatherforecast.model.Weather>,
        val clouds: com.example.weatherforecast.model.Clouds,
        val wind: com.example.weatherforecast.model.Wind,
        val visibility: Int,
        val pop: Double,
        val sys: ForecastSys,
        val dt_txt: String
    )
    
    data class ForecastSys(
        val pod: String
    )
    
    data class City(
        val id: Long,
        val name: String,
        val coord: com.example.weatherforecast.model.Coord,
        val country: String,
        val population: Long,
        val timezone: Int,
        val sunrise: Long,
        val sunset: Long
    )
}