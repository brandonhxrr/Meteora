package ipn.escom.meteora.data.weather

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

data class Weather(
    val coord: Coord,
    val weather: List<WeatherDetail>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val rain: Rain?,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Long,
    val name: String,
    val cod: Int
)

data class Coord(
    val lon: Double,
    val lat: Double
)

@Immutable
data class WeatherDetail(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("pressure")
    val pressure: Int,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("sea_level")
    val seaLevel: Int,
    @SerializedName("grnd_level")
    val groundLevel: Int
)

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double
)

data class Rain(
    @SerializedName("1h")
    val oneHour: Double
)

data class Clouds(
    val all: Int
)

data class Sys(
    val type: Int,
    val id: Long,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)
