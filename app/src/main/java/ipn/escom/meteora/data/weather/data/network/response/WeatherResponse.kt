package ipn.escom.meteora.data.weather.data.network.response

import ipn.escom.meteora.data.weather.Main
import ipn.escom.meteora.data.weather.WeatherDetail
import ipn.escom.meteora.data.weather.Wind

data class WeatherResponse (
    val main: Main,
    val weather: List<WeatherDetail>,
    val wind: Wind,
    val name: String
)