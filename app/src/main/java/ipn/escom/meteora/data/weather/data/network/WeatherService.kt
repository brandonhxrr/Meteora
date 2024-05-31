package ipn.escom.meteora.data.weather.data.network

import android.content.Context
import android.util.Log
import androidx.room.Room
import ipn.escom.meteora.core.network.RetrofitHelper
import ipn.escom.meteora.data.weather.data.local.WeatherDatabase
import ipn.escom.meteora.data.weather.data.local.entities.DailyForecastEntity
import ipn.escom.meteora.data.weather.data.local.entities.HourlyForecastEntity
import ipn.escom.meteora.data.weather.data.local.entities.WeatherEntity
import ipn.escom.meteora.data.weather.data.network.response.DailyForecastResponse
import ipn.escom.meteora.data.weather.data.network.response.HourlyForecastResponse
import ipn.escom.meteora.data.weather.data.network.response.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class WeatherService(context: Context) {
    private val retrofitAPI = RetrofitHelper.getRetrofit("https://api.openweathermap.org/data/2.5/")
    private val retrofitPro = RetrofitHelper.getRetrofit("https://pro.openweathermap.org/data/2.5/")

    private val db = Room.databaseBuilder(
        context.applicationContext,
        WeatherDatabase::class.java, "weather_database"
    ).build()

    private val weatherDao = db.weatherDao()

    suspend fun getWeather(apiKey: String, lat: Double, lon: Double): WeatherResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    retrofitAPI.create(WeatherClient::class.java).getWeather(lat, lon, apiKey)
                Log.d("WeatherService", "getWeather: ${response.body()}")
                response.body() ?: WeatherResponse()
            } catch (e: SocketTimeoutException) {
                Log.e("WeatherService", "Timeout al conectar a la API")
                delay(2000)
                getWeather(apiKey, lat, lon)
                WeatherResponse()
            } catch (e: UnknownHostException) {
                Log.e("WeatherService", "No internet connection: ${e.message}")
                WeatherResponse()
            } catch (e: Exception) {
                Log.e("WeatherService", "Error getting weather: ${e.message}")
                WeatherResponse()
            }
        }
    }

    suspend fun getHourlyForecast(
        apiKey: String,
        lat: Double,
        lon: Double
    ): HourlyForecastResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    retrofitPro.create(WeatherClient::class.java)
                        .getHourlyForecast(lat, lon, apiKey)
                Log.d("WeatherService", "getHourlyForecast: ${response.body()}")
                response.body() ?: HourlyForecastResponse()
            } catch (e: SocketTimeoutException) {
                Log.e("WeatherService", "Timeout al conectar a la API")
                delay(2000)
                getHourlyForecast(apiKey, lat, lon)
                HourlyForecastResponse()
            } catch (e: UnknownHostException) {
                Log.e("WeatherService", "No internet connection: ${e.message}")
                HourlyForecastResponse()
            } catch (e: Exception) {
                Log.e("WeatherService", "Error getting hourly forecast: ${e.message}")
                HourlyForecastResponse()
            }
        }
    }

    suspend fun getDailyForecast(
        apiKey: String,
        lat: Double,
        lon: Double
    ): DailyForecastResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    retrofitPro.create(WeatherClient::class.java)
                        .getDailyForecast(lat, lon, apiKey)
                Log.d("WeatherService", "getDailyForecast: ${response.body()}")
                response.body()?: DailyForecastResponse()
            } catch (e: SocketTimeoutException) {
                Log.e("WeatherService", "Timeout al conectar a la API")
                delay(2000)
                getDailyForecast(apiKey, lat, lon)
                DailyForecastResponse()
            } catch (e: UnknownHostException) {
                Log.e("WeatherService", "No internet connection: ${e.message}")
                DailyForecastResponse()
            } catch (e: Exception) {
                Log.e("WeatherService", "Error getting daily forecast: ${e.message}")
                DailyForecastResponse()
            }
        }
    }

    suspend fun saveWeather(weather: WeatherResponse) {
        try {
            val weatherEntity = WeatherEntity(
                id = 0,
                coord = weather.coord,
                weather = weather.weather,
                base = weather.base,
                main = weather.main,
                visibility = weather.visibility,
                wind = weather.wind,
                rain = weather.rain,
                clouds = weather.clouds,
                dt = weather.dt,
                sys = weather.sys,
                timezone = weather.timezone,
                name = weather.name,
                cod = weather.cod
            )
            weatherDao.insertWeather(weatherEntity)
        } catch (e: Exception) {
            Log.e("WeatherService", "Error saving weather: ${e.message}", e)
        }
    }

    suspend fun getSavedWeather(): WeatherEntity? {
        return try {
            weatherDao.getWeather()
        } catch (e: Exception) {
            Log.e("WeatherService", "Error getting saved weather: ${e.message}", e)
            null
        }
    }

    suspend fun saveHourlyForecast(hourlyForecast: HourlyForecastResponse) {
        try {
            val hourlyForecastEntity = HourlyForecastEntity(
                id = 0,
                cod = hourlyForecast.cod,
                message = hourlyForecast.message,
                cnt = hourlyForecast.cnt,
                list = hourlyForecast.list,
                city = hourlyForecast.city
            )
            weatherDao.insertHourlyForecast(hourlyForecastEntity)
        } catch (e: Exception) {
            Log.e("WeatherService", "Error saving hourly forecast: ${e.message}", e)
        }
    }

    suspend fun getSavedHourlyForecast(): HourlyForecastEntity? {
        return try {
            weatherDao.getHourlyForecast()
        } catch (e: Exception) {
            Log.e("WeatherService", "Error getting saved hourly forecast: ${e.message}", e)
            null
        }
    }

    suspend fun saveDailyForecast(dailyForecast: DailyForecastResponse) {
        try {
            val dailyForecastEntity = DailyForecastEntity(
                id = 0,
                cod = dailyForecast.cod,
                message = dailyForecast.message,
                cnt = dailyForecast.cnt,
                list = dailyForecast.list,
                city = dailyForecast.city
            )
            weatherDao.insertDailyForecast(dailyForecastEntity)
        } catch (e: Exception) {
            Log.e("WeatherService", "Error saving daily forecast: ${e.message}", e)
        }
    }

    suspend fun getSavedDailyForecast(): DailyForecastEntity? {
        return try {
            weatherDao.getDailyForecast()
        } catch (e: Exception) {
            Log.e("WeatherService", "Error getting saved daily forecast: ${e.message}", e)
            null
        }
    }
}