package ipn.escom.meteora.data.weather

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
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherUseCase = WeatherUseCase()
    private val dailyForecastUseCase = DailyForecastUseCase()
    private val hourlyForecastUseCase = HourlyForecastUseCase()

    private val _weather = MutableLiveData<WeatherResponse?>()
    val weather: LiveData<WeatherResponse?> = _weather

    private val _hourlyForecast = MutableLiveData<HourlyForecastResponse?>()
    val hourlyForecast: LiveData<HourlyForecastResponse?> = _hourlyForecast

    private val _dailyForecast = MutableLiveData<DailyForecastResponse?>()
    val dailyForecast: LiveData<DailyForecastResponse?> = _dailyForecast

    fun getWeather(apiKey: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            val response = weatherUseCase(apiKey, lat, lon)
            _weather.value = response
        }
    }

    fun getHourlyForecast(apiKey: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            val response = hourlyForecastUseCase(apiKey, lat, lon)
            _hourlyForecast.value = response
        }
    }

    fun getDailyForecast(apiKey: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            val response = dailyForecastUseCase(apiKey, lat, lon)
            _dailyForecast.value = response
        }
    }
}
