package ipn.escom.meteora.data.weather.data

import android.content.Context
import ipn.escom.meteora.data.weather.data.network.WeatherService
import ipn.escom.meteora.data.weather.data.network.response.DailyForecastResponse
import ipn.escom.meteora.data.weather.data.network.response.HourlyForecastResponse
import ipn.escom.meteora.data.weather.data.network.response.WeatherResponse

class WeatherRepository(context: Context) {
    private val api = WeatherService(context)

    suspend fun getWeather(apiKey: String, lat: Double, lon: Double): WeatherResponse =
        api.getWeather(apiKey, lat, lon)

    suspend fun getHourlyForecast(
        apiKey: String,
        lat: Double,
        lon: Double
    ): HourlyForecastResponse = api.getHourlyForecast(apiKey, lat, lon)

    suspend fun getDailyForecast(apiKey: String, lat: Double, lon: Double): DailyForecastResponse =
        api.getDailyForecast(apiKey, lat, lon)

    suspend fun saveWeather(weather: WeatherResponse) = api.saveWeather(weather)

    suspend fun saveHourlyForecast(hourlyForecast: HourlyForecastResponse) =
        api.saveHourlyForecast(hourlyForecast)

    suspend fun saveDailyForecast(dailyForecast: DailyForecastResponse) =
        api.saveDailyForecast(dailyForecast)

    suspend fun getSavedWeather() = api.getSavedWeather()

    suspend fun getSavedHourlyForecast() = api.getSavedHourlyForecast()

    suspend fun getSavedDailyForecast() = api.getSavedDailyForecast()

}