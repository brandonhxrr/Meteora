package ipn.escom.meteora.data.weather

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import ipn.escom.meteora.R

data class WeatherCondition(
    val location: String,
    val temperature: Double,
    val feelsLike: Double,
    val description: String,
    val code: String
) {

    fun getDescription(): Int {
        return when (code) {
            "01d" -> R.string.condition_sunny
            "01n" -> R.string.condition_clear
            "02d", "02n" -> R.string.condition_partly_cloudy
            "03d", "03n", "04d", "04n" -> R.string.condition_cloudy
            "09d", "09n", "10d", "10n" -> R.string.condition_rain
            "11d", "11n" -> R.string.condition_thunderstorm
            "13d", "13n" -> R.string.condition_snow
            "50d", "50n" -> R.string.condition_mist
            else -> R.string.condition_sunny
        }
    }

    @DrawableRes
    fun getIconDrawable(): Int {
        return when (code) {
            "01d" -> R.drawable.ic_weather_sunny
            "01n" -> R.drawable.ic_weather_night
            "02d" -> R.drawable.ic_weather_partly_cloudy
            "02n" -> R.drawable.ic_weather_cloudynight
            "03d", "03n", "04d", "04n" -> R.drawable.ic_weather_windy
            "09d", "10d" -> R.drawable.ic_weather_partly_shower
            "09n", "10n" -> R.drawable.ic_weather_rainynight
            "11d" -> R.drawable.ic_weather_stormshowersday
            "11n" -> R.drawable.ic_weather_storm
            "13d" -> R.drawable.ic_weather_snow_sunny
            "13n" -> R.drawable.ic_weather_snownight
            "50d", "50n" -> R.drawable.ic_weather_mist
            else -> R.drawable.ic_weather_sunny
        }
    }

    @RawRes
    fun getAnimatedIcon(): Int {
        return getAnimatedIcon(code)
    }
}

@RawRes
fun getAnimatedIcon(code: String): Int {
    return when (code) {
        "01d" -> R.raw.weather_sunny
        "01n" -> R.raw.weather_night
        "02d" -> R.raw.weather_partly_cloudy
        "02n" -> R.raw.weather_cloudynight
        "03d", "03n", "04d", "04n" -> R.raw.weather_windy
        "09d", "09n", "10d", "10n" -> R.raw.weather_partly_shower
        "11d" -> R.raw.weather_stormshowersday
        "11n" -> R.raw.weather_storm
        "13d" -> R.raw.weather_snow_sunny
        "13n" -> R.raw.weather_snownight
        "50d", "50n" -> R.raw.weather_mist
        else -> R.raw.weather_sunny
    }
}