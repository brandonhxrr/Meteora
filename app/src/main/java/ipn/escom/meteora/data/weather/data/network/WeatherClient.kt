package ipn.escom.meteora.data.weather.data.network

import ipn.escom.meteora.data.weather.Weather
import ipn.escom.meteora.data.weather.data.network.response.DailyForecastResponse
import ipn.escom.meteora.data.weather.data.network.response.HourlyForecastResponse
import ipn.escom.meteora.utils.calculateTimestamps
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherClient {
    @GET("weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): Response<Weather>

    @GET("forecast/hourly")
    suspend fun getHourlyForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): Response<HourlyForecastResponse>

    @GET("forecast/daily")
    suspend fun getDailyForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): Response<DailyForecastResponse>


}
