package com.chotu.weatheruiclone.data.model

data class ForecastResponse(
    val list: List<ForecastItem>
)

data class ForecastItem(
    val dt_txt: String,
    val main: ForecastMain,
    val weather: List<ForecastWeather>
)

data class ForecastMain(
    val temp: Double
)

data class ForecastWeather(
    val description: String
)

data class ForecastUiModel(
    val time: String,
    val weather: String,
    val temperature: String
)