package ipn.escom.meteora.data.weather.data

import ipn.escom.meteora.data.weather.data.network.WeatherService

class WeatherRepository {
    private val api = WeatherService()

    suspend fun getWeather(apiKey: String, lat: Double, lon: Double) = api.getWeather(apiKey, lat, lon)

    suspend fun getHourlyForecast(apiKey: String, lat: Double, lon: Double) = api.getHourlyForecast(apiKey, lat, lon)

    suspend fun getDailyForecast(apiKey: String, lat: Double, lon: Double) = api.getDailyForecast(apiKey, lat, lon)

}