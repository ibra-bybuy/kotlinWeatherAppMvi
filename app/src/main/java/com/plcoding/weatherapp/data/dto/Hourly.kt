package com.plcoding.weatherapp.data.dto

import com.plcoding.weatherapp.domain.entities.WeatherData
import com.plcoding.weatherapp.domain.weather.WeatherType
import com.squareup.moshi.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Hourly(
    @field:Json(name = "temperature_2m")
    val temperatures: List<Double> = emptyList(),
    @field:Json(name = "time")
    val time: List<String> = emptyList(),
    @field:Json(name = "pressure_msl")
    val pressures: List<Double> = emptyList(),
    @field:Json(name = "relativehumidity_2m")
    val relativeHumidity: List<Int> = emptyList(),
    @field:Json(name = "weathercode")
    val weatherCodes: List<Int> = emptyList(),
    @field:Json(name = "windspeed_10m")
    val windSpeeds: List<Double> = emptyList()
)

fun Hourly.toWeatherDataMap() : Map<Int, List<WeatherData>> {
    return time.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        val windSpeed = windSpeeds[index]
        val pressure = pressures[index]
        val humidity = relativeHumidity[index]
        IndexedWeatherData(
            index = index,
            data = WeatherData(
                time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                temperatureCelsius = temperature,
                pressure = pressure,
                windSpeed = windSpeed,
                humidity = humidity,
                weatherType = WeatherType.fromWMO(weatherCode)
            )
        )
    }.groupBy {
        it.index / 24
    }.mapValues {
        it.value.map {
            it.data
        }
    }
}

private data class IndexedWeatherData(
    val index: Int,
    val data: WeatherData
)