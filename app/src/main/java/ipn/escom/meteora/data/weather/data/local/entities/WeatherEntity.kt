package ipn.escom.meteora.data.weather.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ipn.escom.meteora.data.weather.data.local.converters.Converters
import ipn.escom.meteora.data.weather.data.network.response.Coord
import ipn.escom.meteora.data.weather.data.network.response.Main
import ipn.escom.meteora.data.weather.data.network.response.Rain
import ipn.escom.meteora.data.weather.data.network.response.WeatherDetail
import ipn.escom.meteora.data.weather.data.network.response.Wind
import ipn.escom.meteora.data.weather.data.network.response.Clouds
import ipn.escom.meteora.data.weather.data.network.response.Sys

@Entity(tableName = "weather")
@TypeConverters(Converters::class)
data class WeatherEntity(
    @PrimaryKey val id: Int = 0,
    val coord: Coord = Coord(),
    val weather: List<WeatherDetail> = listOf(WeatherDetail()),
    val base: String = "",
    val main: Main = Main(),
    val visibility: Int = 0,
    val wind: Wind = Wind(),
    val rain: Rain? = Rain(),
    val clouds: Clouds = Clouds(),
    val dt: Long = 0,
    val sys: Sys = Sys(),
    val timezone: Int = 0,
    val name: String = "",
    val cod: Int = 0
)

