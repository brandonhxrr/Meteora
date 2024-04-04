package ipn.escom.meteora.data.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipn.escom.meteora.data.weather.domain.WeatherUseCase
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    val weatherUseCase = WeatherUseCase()

    private val _temperature = MutableLiveData<Double>()
    val temperature: LiveData<Double> = _temperature

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _feelsLike = MutableLiveData<Double>()
    val feelsLike: LiveData<Double> = _feelsLike

    private val _humidity = MutableLiveData<Int>()
    val humidity: LiveData<Int> = _humidity

    private val _windSpeed = MutableLiveData<Double>()
    val windSpeed: LiveData<Double> = _windSpeed

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _weather = MutableLiveData<Weather>()
    val weather: LiveData<Weather> = _weather

    fun getWeather(apiKey: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            val response = weatherUseCase(apiKey, lat, lon)

            if (response != null) {
                _temperature.value = response.main.temp
                _description.value = response.weather[0].description
                _feelsLike.value = response.main.feelsLike
                _humidity.value = response.main.humidity
                _windSpeed.value = response.wind.speed
                _name.value = response.name
            }
        }
    }
}
