package ipn.escom.meteora.data.weather.domain

import ipn.escom.meteora.data.weather.data.WeatherRepository
import ipn.escom.meteora.data.weather.data.network.response.HourlyForecastResponse

class HourlyForecastUseCase {
    private val repository = WeatherRepository()

    suspend operator fun invoke(apiKey: String, lat: Double, lon: Double): HourlyForecastResponse? {
        return repository.getHourlyForecast(apiKey, lat, lon)
    }
}