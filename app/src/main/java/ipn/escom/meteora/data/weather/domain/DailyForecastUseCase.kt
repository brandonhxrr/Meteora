package ipn.escom.meteora.data.weather.domain

import android.content.Context
import ipn.escom.meteora.data.weather.data.WeatherRepository
import ipn.escom.meteora.data.weather.data.network.response.DailyForecastResponse

class DailyForecastUseCase(context: Context) {
    private val repository = WeatherRepository(context)

    suspend operator fun invoke(apiKey: String, lat: Double, lon: Double, units: String): DailyForecastResponse {
        return repository.getDailyForecast(apiKey, lat, lon, units)
    }

    suspend fun saveDailyForecast(dailyForecast: DailyForecastResponse) =
        repository.saveDailyForecast(dailyForecast)

    suspend fun getSavedDailyForecast() = repository.getSavedDailyForecast()
}