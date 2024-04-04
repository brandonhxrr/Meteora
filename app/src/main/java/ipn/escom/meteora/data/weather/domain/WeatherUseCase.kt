package ipn.escom.meteora.data.weather.domain

import ipn.escom.meteora.data.weather.Weather
import ipn.escom.meteora.data.weather.data.WeatherRepository

class WeatherUseCase {

    private val repository = WeatherRepository()

    suspend operator fun invoke(apiKey: String, lat: Double, lon: Double): Weather? {
        return repository.getWeather(apiKey, lat, lon)
    }

}