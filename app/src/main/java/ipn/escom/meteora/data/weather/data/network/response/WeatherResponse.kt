package ipn.escom.meteora.data.weather.data.network.response

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

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

data class Coord(
    val lon: Double = 0.0,
    val lat: Double = 0.0
)

@Immutable
data class WeatherDetail(
    val id: Int = 0,
    val main: String = "",
    val description: String = "",
    val icon: String = ""
)

data class Main(
    @SerializedName("temp")
    val temp: Double = 0.0,
    @SerializedName("feels_like")
    val feelsLike: Double = 0.0,
    @SerializedName("temp_min")
    val tempMin: Double = 0.0,
    @SerializedName("temp_max")
    val tempMax: Double = 0.0,
    @SerializedName("pressure")
    val pressure: Int = 0,
    @SerializedName("humidity")
    val humidity: Int = 0,
    @SerializedName("sea_level")
    val seaLevel: Int = 0,
    @SerializedName("grnd_level")
    val groundLevel: Int = 0
)

data class Wind(
    val speed: Double = 0.0,
    val deg: Int = 0,
    val gust: Double = 0.0
)

data class Rain(
    @SerializedName("1h")
    val oneHour: Double = 0.0,
)

data class Clouds(
    val all: Int = 0
)

data class Sys(
    val type: Int = 0,
    val id: Long = 0,
    val country: String = "",
    val sunrise: Long = 0,
    val sunset: Long = 0
)

fun formatTemperature(temp: Double, showDecimals: Boolean) = "${if(showDecimals) temp else temp.toInt()}°"
fun formatRain(rain: Double) = "$rain mm"