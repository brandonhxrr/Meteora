package ipn.escom.meteora.data.weather.domain

import ipn.escom.meteora.data.weather.Weather
import ipn.escom.meteora.data.weather.data.WeatherRepository
import ipn.escom.meteora.data.weather.data.network.response.WeatherResponse

class WeatherUseCase {

    private val repository = WeatherRepository()

    suspend operator fun invoke(apiKey: String, lat: Double, lon: Double): WeatherResponse? {
        return repository.getWeather(apiKey, lat, lon) as WeatherResponse
    }



}