package com.plcoding.weatherapp.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {

    var state by mutableStateOf(WeatherState())
        private set

    fun loadWeatherInfo() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )

            locationTracker.getCurrentLocation()?.let { location ->
                getRequest(location.latitude, location.longitude)
            } ?: kotlin.run {
                getRequest(53.0, 13.0)
            }
        }
    }

    private suspend fun getRequest(lat: Double, lng: Double) {
        when(val result = repository.getWeatherData(lat, lng)) {
            is Resource.Success -> {
                state = state.copy(
                    isLoading = false,
                    error = null,
                    weatherInfo = result.data
                )
            }
            is Resource.Error -> {
                state = state.copy(
                    weatherInfo = null,
                    isLoading = false,
                    error = result.message
                )
            }
        }
    }
}