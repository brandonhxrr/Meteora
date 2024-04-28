package ipn.escom.meteora.data.weather.data.network.response

import ipn.escom.meteora.data.weather.Clouds
import ipn.escom.meteora.data.weather.Coord
import ipn.escom.meteora.data.weather.Main
import ipn.escom.meteora.data.weather.Rain
import ipn.escom.meteora.data.weather.Sys
import ipn.escom.meteora.data.weather.WeatherDetail
import ipn.escom.meteora.data.weather.Wind

data class HourlyForecastResponse(
    val cod: String,
    val message: Double,
    val cnt: Int,
    val list: List<HourlyForecast>,
    val city: City
)

data class HourlyForecast(
    val dt: Long,
    val main: Main,
    val weather: List<WeatherDetail>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    val rain: Rain?,
    val sys: Sys,
    val dt_txt: String
)

data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)