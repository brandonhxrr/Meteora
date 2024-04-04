package ipn.escom.meteora.data.weather.data.network

import android.util.Log
import ipn.escom.meteora.core.network.RetrofitHelper
import ipn.escom.meteora.data.weather.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherService {
    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getWeather(apiKey: String, lat: Double, lon: Double): Weather? {
        return withContext(Dispatchers.IO){
            val response = retrofit.create(WeatherClient::class.java).getWeather(lat, lon, apiKey)
            Log.d("WeatherService", "getWeather: ${response.body()}")
            response.body()
        }
    }
}