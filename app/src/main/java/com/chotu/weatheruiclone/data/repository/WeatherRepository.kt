package com.chotu.weatheruiclone.data.repository

import com.chotu.weatheruiclone.BuildConfig
import com.chotu.weatheruiclone.data.api.RetrofitInstance
import com.chotu.weatheruiclone.data.model.ForecastResponse
import com.chotu.weatheruiclone.data.model.WeatherResponse
import retrofit2.Retrofit

class WeatherRepository {
    suspend fun getWeather(city: String): WeatherResponse{
        return RetrofitInstance.api.getWeather(
            city = city,
            apiKey =  BuildConfig.WEATHER_API_KEY
        )
    }

    suspend fun getForecast(city: String): ForecastResponse {

        return RetrofitInstance.api.getForecast(
            city = city,
            apiKey = BuildConfig.WEATHER_API_KEY
        )
    }
}