package ipn.escom.meteora.data.weather.data.network.response

import ipn.escom.meteora.data.weather.Clouds
import ipn.escom.meteora.data.weather.Coord
import ipn.escom.meteora.data.weather.Main
import ipn.escom.meteora.data.weather.Rain
import ipn.escom.meteora.data.weather.Sys
import ipn.escom.meteora.data.weather.WeatherDetail
import ipn.escom.meteora.data.weather.Wind

data class WeatherResponse(
    val coord: Coord = Coord(),
    val weather: List<WeatherDetail> = listOf(WeatherDetail()),
    val base: String = "",
    val main: Main = Main(),
    val visibility: Int = 0,
    val wind: Wind = Wind(),
    val rain: Rain = Rain(),
    val clouds: Clouds = Clouds(),
    val dt: Long = 0,
    val sys: Sys = Sys(),
    val timezone: Int = 0,
    val id: Long = 0,
    val name: String = "",
    val cod: Int = 0
)