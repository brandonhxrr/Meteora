package ipn.escom.meteora.data.weather

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipn.escom.meteora.data.weather.data.network.response.DailyForecastResponse
import ipn.escom.meteora.data.weather.data.network.response.HourlyForecastResponse
import ipn.escom.meteora.data.weather.data.network.response.WeatherResponse
import ipn.escom.meteora.data.weather.domain.DailyForecastUseCase
import ipn.escom.meteora.data.weather.domain.HourlyForecastUseCase
import ipn.escom.meteora.data.weather.domain.WeatherUseCase
import ipn.escom.meteora.utils.toDailyForecastResponse
import ipn.escom.meteora.utils.toHourlyForecastResponse
import ipn.escom.meteora.utils.toWeatherResponse
import kotlinx.coroutines.launch

class WeatherViewModel(context: Context) : ViewModel() {

    private val weatherUseCase = WeatherUseCase(context)
    private val dailyForecastUseCase = DailyForecastUseCase(context)
    private val hourlyForecastUseCase = HourlyForecastUseCase(context)

    private val _weather = MutableLiveData<WeatherResponse>()
    val weather: LiveData<WeatherResponse> = _weather

    private val _hourlyForecast = MutableLiveData<HourlyForecastResponse>()
    val hourlyForecast: LiveData<HourlyForecastResponse> = _hourlyForecast

    private val _dailyForecast = MutableLiveData<DailyForecastResponse>()
    val dailyForecast: LiveData<DailyForecastResponse> = _dailyForecast

    init {
        _weather.value = WeatherResponse()
        _hourlyForecast.value = HourlyForecastResponse()
        _dailyForecast.value = DailyForecastResponse()

        viewModelScope.launch {
            loadSavedData()
        }
    }

    private suspend fun loadSavedData() {
        val savedWeather = weatherUseCase.getSavedWeather()
        _weather.value = savedWeather?.toWeatherResponse() ?: WeatherResponse()

        val savedHourlyForecast = hourlyForecastUseCase.getSavedHourlyForecast()
        _hourlyForecast.value = savedHourlyForecast?.toHourlyForecastResponse() ?: HourlyForecastResponse()

        val savedDailyForecast = dailyForecastUseCase.getSavedDailyForecast()
        _dailyForecast.value = savedDailyForecast?.toDailyForecastResponse() ?: DailyForecastResponse()
    }

    fun getWeather(apiKey: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            val response = weatherUseCase(apiKey, lat, lon)
            _weather.value = response

            if (response != WeatherResponse()) {
                _weather.value = response
                weatherUseCase.saveWeather(response)
            } else {
                val savedWeather = weatherUseCase.getSavedWeather()
                _weather.value = savedWeather?.toWeatherResponse() ?: WeatherResponse()
            }
        }
    }

    fun getHourlyForecast(apiKey: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            val response = hourlyForecastUseCase(apiKey, lat, lon)
            if (response != HourlyForecastResponse()) {
                _hourlyForecast.value = response
                hourlyForecastUseCase.saveHourlyForecast(response)
            } else {
                val savedHourlyForecast = hourlyForecastUseCase.getSavedHourlyForecast()
                _hourlyForecast.value = savedHourlyForecast?.toHourlyForecastResponse() ?: HourlyForecastResponse()
            }

        }
    }

    fun getDailyForecast(apiKey: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            val response = dailyForecastUseCase(apiKey, lat, lon)
            if (response != DailyForecastResponse()) {
                _dailyForecast.value = response
                dailyForecastUseCase.saveDailyForecast(response)
            } else {
                val savedDailyForecast = dailyForecastUseCase.getSavedDailyForecast()
                _dailyForecast.value = savedDailyForecast?.toDailyForecastResponse() ?: DailyForecastResponse()
            }
        }
    }
}
