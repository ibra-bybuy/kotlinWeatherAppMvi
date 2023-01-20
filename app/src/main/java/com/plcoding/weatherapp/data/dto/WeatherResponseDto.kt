package com.plcoding.weatherapp.data.dto

import com.plcoding.weatherapp.domain.entities.WeatherData
import com.plcoding.weatherapp.domain.entities.WeatherInfo
import com.squareup.moshi.Json
import java.time.LocalDateTime

data class WeatherResponseDto(
    @field:Json(name = "hourly")
    val hourly: Hourly = Hourly(),
)

fun WeatherResponseDto.toWeatherInfo(): WeatherInfo {
    val weatherDataMap = hourly.toWeatherDataMap()
    val now = LocalDateTime.now()
    val currentWeatherData = weatherDataMap[0]?.find {
        val hour = if(now.minute < 30) now.hour else now.hour + 1
        it.time.hour == hour
    }

    return WeatherInfo(
        weatherDataPerDay = weatherDataMap,
        currentWeatherData = currentWeatherData
    )
}