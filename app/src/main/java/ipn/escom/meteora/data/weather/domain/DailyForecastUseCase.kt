package ipn.escom.meteora.data.weather.domain

import ipn.escom.meteora.data.weather.data.WeatherRepository
import ipn.escom.meteora.data.weather.data.network.response.DailyForecastResponse

class DailyForecastUseCase {
    private val repository = WeatherRepository()

    suspend operator fun invoke(apiKey: String, lat: Double, lon: Double): DailyForecastResponse? {
        return repository.getDailyForecast(apiKey, lat, lon)
    }
}