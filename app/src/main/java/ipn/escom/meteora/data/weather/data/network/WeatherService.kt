package ipn.escom.meteora.data.weather.data.network

import android.util.Log
import ipn.escom.meteora.core.network.RetrofitHelper
import ipn.escom.meteora.data.weather.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class WeatherService {
    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getWeather(apiKey: String, lat: Double, lon: Double): Weather? {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    retrofit.create(WeatherClient::class.java).getWeather(lat, lon, apiKey)
                Log.d("WeatherService", "getWeather: ${response.body()}")
                response.body()
            } catch (e: SocketTimeoutException) {
                Log.e("WeatherService", "Timeout al conectar a la API")
                delay(5000)
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
}