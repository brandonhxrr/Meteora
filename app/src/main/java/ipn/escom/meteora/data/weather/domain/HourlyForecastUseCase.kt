package ipn.escom.meteora.data.weather.domain

import android.content.Context
import ipn.escom.meteora.data.weather.data.WeatherRepository
import ipn.escom.meteora.data.weather.data.network.response.HourlyForecastResponse

class HourlyForecastUseCase(context: Context) {
    private val repository = WeatherRepository(context)

    suspend operator fun invoke(apiKey: String, lat: Double, lon: Double): HourlyForecastResponse {
        return repository.getHourlyForecast(apiKey, lat, lon)
    }

    suspend fun saveHourlyForecast(hourlyForecast: HourlyForecastResponse) =
        repository.saveHourlyForecast(hourlyForecast)

    suspend fun getSavedHourlyForecast() = repository.getSavedHourlyForecast()
}