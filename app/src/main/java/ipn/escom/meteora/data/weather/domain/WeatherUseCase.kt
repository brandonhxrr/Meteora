package ipn.escom.meteora.data.weather.domain

import android.content.Context
import ipn.escom.meteora.data.weather.data.WeatherRepository
import ipn.escom.meteora.data.weather.data.network.response.WeatherResponse

class WeatherUseCase(context: Context) {

    private val repository = WeatherRepository(context)

    suspend operator fun invoke(apiKey: String, lat: Double, lon: Double, units: String): WeatherResponse {
        return repository.getWeather(apiKey, lat, lon, units)
    }

    suspend fun saveWeather(weather: WeatherResponse) = repository.saveWeather(weather)

    suspend fun getSavedWeather() = repository.getSavedWeather()
}