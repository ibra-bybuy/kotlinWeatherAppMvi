package com.plcoding.weatherapp.data.repository

import com.plcoding.weatherapp.data.dto.toWeatherInfo
import com.plcoding.weatherapp.data.remote.WeatherApi
import com.plcoding.weatherapp.domain.entities.WeatherInfo
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.Resource
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor (
    private val api: WeatherApi
) : WeatherRepository {
    override suspend fun getWeatherData(lat: Double, lng: Double): Resource<WeatherInfo> {
        return try {
            Resource.Success(
                data = api.getWeatherData(
                    lat = lat,
                    lng = lng
                ).toWeatherInfo()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "")
        }
    }
}