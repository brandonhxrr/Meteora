package ipn.escom.meteora.data.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipn.escom.meteora.data.weather.domain.WeatherUseCase
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherUseCase = WeatherUseCase()

    private val _coordLon = MutableLiveData<Double>()
    val coordLon: LiveData<Double> = _coordLon

    private val _coordLat = MutableLiveData<Double>()
    val coordLat: LiveData<Double> = _coordLat

    private val _weatherId = MutableLiveData<Int>()
    val weatherId: LiveData<Int> = _weatherId

    private val _weatherMain = MutableLiveData<String>()
    val weatherMain: LiveData<String> = _weatherMain

    private val _weatherDescription = MutableLiveData<String>()
    val weatherDescription: LiveData<String> = _weatherDescription

    private val _weatherIcon = MutableLiveData<String>()
    val weatherIcon: LiveData<String> = _weatherIcon

    private val _base = MutableLiveData<String>()
    val base: LiveData<String> = _base

    private val _mainTemp = MutableLiveData<Double>()
    val mainTemp: LiveData<Double> = _mainTemp

    private val _mainFeelsLike = MutableLiveData<Double>()
    val mainFeelsLike: LiveData<Double> = _mainFeelsLike

    private val _mainTempMin = MutableLiveData<Double>()
    val mainTempMin: LiveData<Double> = _mainTempMin

    private val _mainTempMax = MutableLiveData<Double>()
    val mainTempMax: LiveData<Double> = _mainTempMax

    private val _mainPressure = MutableLiveData<Int>()
    val mainPressure: LiveData<Int> = _mainPressure

    private val _mainHumidity = MutableLiveData<Int>()
    val mainHumidity: LiveData<Int> = _mainHumidity

    private val _mainSeaLevel = MutableLiveData<Int>()
    val mainSeaLevel: LiveData<Int> = _mainSeaLevel

    private val _mainGroundLevel = MutableLiveData<Int>()
    val mainGroundLevel: LiveData<Int> = _mainGroundLevel

    private val _visibility = MutableLiveData<Int>()
    val visibility: LiveData<Int> = _visibility

    private val _windSpeed = MutableLiveData<Double>()
    val windSpeed: LiveData<Double> = _windSpeed

    private val _windDeg = MutableLiveData<Int>()
    val windDeg: LiveData<Int> = _windDeg

    private val _windGust = MutableLiveData<Double>()
    val windGust: LiveData<Double> = _windGust

    private val _rain1h = MutableLiveData<Double>()
    val rain1h: LiveData<Double> = _rain1h

    private val _cloudsAll = MutableLiveData<Int>()
    val cloudsAll: LiveData<Int> = _cloudsAll

    private val _dt = MutableLiveData<Long>()
    val dt: LiveData<Long> = _dt

    private val _sysType = MutableLiveData<Int>()
    val sysType: LiveData<Int> = _sysType

    private val _sysId = MutableLiveData<Long>()
    val sysId: LiveData<Long> = _sysId

    private val _sysCountry = MutableLiveData<String>()
    val sysCountry: LiveData<String> = _sysCountry

    private val _sysSunrise = MutableLiveData<Long>()
    val sysSunrise: LiveData<Long> = _sysSunrise

    private val _sysSunset = MutableLiveData<Long>()
    val sysSunset: LiveData<Long> = _sysSunset

    private val _timezone = MutableLiveData<Int>()
    val timezone: LiveData<Int> = _timezone

    private val _id = MutableLiveData<Long>()
    val id: LiveData<Long> = _id

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _cod = MutableLiveData<Int>()
    val cod: LiveData<Int> = _cod

    fun getWeather(apiKey: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            val response = weatherUseCase(apiKey, lat, lon)

            if (response != null) {
                _coordLon.value = response.coord.lon
                _coordLat.value = response.coord.lat
                _weatherId.value = response.weather[0].id
                _weatherMain.value = response.weather[0].main
                _weatherDescription.value = response.weather[0].description
                _weatherIcon.value = response.weather[0].icon
                _base.value = response.base
                _mainTemp.value = response.main.temp
                _mainFeelsLike.value = response.main.feelsLike
                _mainTempMin.value = response.main.tempMin
                _mainTempMax.value = response.main.tempMax
                _mainPressure.value = response.main.pressure
                _mainHumidity.value = response.main.humidity
                _mainSeaLevel.value = response.main.seaLevel
                _mainGroundLevel.value = response.main.groundLevel
                _visibility.value = response.visibility
                _windSpeed.value = response.wind.speed
                _windDeg.value = response.wind.deg
                _windGust.value = response.wind.gust
                _rain1h.value = response.rain?.oneHour ?: 0.0
                _cloudsAll.value = response.clouds.all
                _dt.value = response.dt
                _sysType.value = response.sys.type
                _sysId.value = response.sys.id
                _sysCountry.value = response.sys.country
                _sysSunrise.value = response.sys.sunrise
                _sysSunset.value = response.sys.sunset
                _timezone.value = response.timezone
                _id.value = response.id
                _name.value = response.name
                _cod.value = response.cod
            }
        }
    }
}
