package ipn.escom.meteora.data.weather.data.network

import android.util.Log
import ipn.escom.meteora.core.network.RetrofitHelper
import ipn.escom.meteora.data.weather.data.network.response.DailyForecastResponse
import ipn.escom.meteora.data.weather.data.network.response.HourlyForecastResponse
import ipn.escom.meteora.data.weather.data.network.response.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class WeatherService {
    private val retrofitAPI = RetrofitHelper.getRetrofit("https://api.openweathermap.org/data/2.5/")
    private val retrofitPro = RetrofitHelper.getRetrofit("https://pro.openweathermap.org/data/2.5/")

    suspend fun getWeather(apiKey: String, lat: Double, lon: Double): WeatherResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    retrofitAPI.create(WeatherClient::class.java).getWeather(lat, lon, apiKey)
                Log.d("WeatherService", "getWeather: ${response.body()}")
                response.body()
            } catch (e: SocketTimeoutException) {
                Log.e("WeatherService", "Timeout al conectar a la API")
                delay(2000)
                getWeather(apiKey, lat, lon)
                null
            } catch (e: UnknownHostException) {
                Log.e("WeatherService", "No internet connection: ${e.message}")
                null
            } catch (e: Exception) {
                Log.e("WeatherService", "Error getting weather: ${e.message}")
                null
            }
        }
    }

    suspend fun getHourlyForecast(
        apiKey: String,
        lat: Double,
        lon: Double
    ): HourlyForecastResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    retrofitPro.create(WeatherClient::class.java)
                        .getHourlyForecast(lat, lon, apiKey)
                Log.d("WeatherService", "getHourlyForecast: ${response.body()}")
                response.body()
            } catch (e: SocketTimeoutException) {
                Log.e("WeatherService", "Timeout al conectar a la API")
                delay(2000)
                getHourlyForecast(apiKey, lat, lon)
                null
            } catch (e: UnknownHostException) {
                Log.e("WeatherService", "No internet connection: ${e.message}")
                null
            } catch (e: Exception) {
                Log.e("WeatherService", "Error getting hourly forecast: ${e.message}")
                null
            }
        }
    }

    suspend fun getDailyForecast(
        apiKey: String,
        lat: Double,
        lon: Double
    ): DailyForecastResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    retrofitPro.create(WeatherClient::class.java)
                        .getDailyForecast(lat, lon, apiKey)
                Log.d("WeatherService", "getDailyForecast: ${response.body()}")
                response.body()
            } catch (e: SocketTimeoutException) {
                Log.e("WeatherService", "Timeout al conectar a la API")
                delay(2000)
                getDailyForecast(apiKey, lat, lon)
                null
            } catch (e: UnknownHostException) {
                Log.e("WeatherService", "No internet connection: ${e.message}")
                null
            } catch (e: Exception) {
                Log.e("WeatherService", "Error getting daily forecast: ${e.message}")
                null
            }
        }
    }
}