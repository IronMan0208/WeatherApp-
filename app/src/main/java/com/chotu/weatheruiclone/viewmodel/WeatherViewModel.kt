package com.chotu.weatheruiclone.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chotu.weatheruiclone.data.model.ForecastItem
import com.chotu.weatheruiclone.data.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository()

    var cityName by mutableStateOf("Loading...")
        private set

    var temperature by mutableStateOf("--")
        private set

    var description by mutableStateOf("Loading...")
        private set

    var humidity by mutableStateOf("--")
        private set

    var windSpeed by mutableStateOf("--")
        private set

    var pressure by mutableStateOf("--")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set

    var forecastList by mutableStateOf<List<ForecastItem>>(emptyList())
        private set

    fun updateWeather(
        cityName: String,
        temperature: String,
        description: String,
        humidity: String,
        windSpeed: String,
        pressure: String
    ) {
        this.cityName = cityName
        this.temperature = temperature
        this.description = description
        this.humidity = humidity
        this.windSpeed = windSpeed
        this.pressure = pressure
    }

    fun updateLoading(value: Boolean) {
        isLoading = value
    }

    fun updateError(message: String) {
        errorMessage = message
    }

    fun getWeather(city: String) {
        viewModelScope.launch {
            try {

                updateLoading(true)

                val response = repository.getWeather(city)

                updateWeather(
                    cityName = response.name,
                    temperature = "%.0f°C".format(response.main.temp),
                    description = response.weather.firstOrNull()?.description ?: "Unknown",
                    humidity = "${response.main.humidity}%",
                    windSpeed = "${response.wind.speed} m/s",
                    pressure = "${response.main.pressure} hPa"
                )

                getForecast(city)
                updateError("")

            } catch (e: Exception) {

                updateError("City Not Found")

            } finally {

                updateLoading(false)
            }
        }
    }

    fun getForecast(city: String) {

        viewModelScope.launch {

            try {

                val response = repository.getForecast(city)

                forecastList = response.list.take(5)

            } catch (e: Exception) {

            }
        }
    }
}