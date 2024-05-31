package ipn.escom.meteora.data.weather.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ipn.escom.meteora.data.weather.data.network.response.*

class Converters {

    @TypeConverter
    fun fromCoord(coord: Coord): String {
        return Gson().toJson(coord)
    }

    @TypeConverter
    fun toCoord(coordString: String): Coord {
        return Gson().fromJson(coordString, Coord::class.java)
    }

    @TypeConverter
    fun fromWeatherDetailList(weatherDetails: List<WeatherDetail>): String {
        return Gson().toJson(weatherDetails)
    }

    @TypeConverter
    fun toWeatherDetailList(weatherDetailString: String): List<WeatherDetail> {
        val type = object : TypeToken<List<WeatherDetail>>() {}.type
        return Gson().fromJson(weatherDetailString, type)
    }

    @TypeConverter
    fun fromMain(main: Main): String {
        return Gson().toJson(main)
    }

    @TypeConverter
    fun toMain(mainString: String): Main {
        return Gson().fromJson(mainString, Main::class.java)
    }

    @TypeConverter
    fun fromWind(wind: Wind): String {
        return Gson().toJson(wind)
    }

    @TypeConverter
    fun toWind(windString: String): Wind {
        return Gson().fromJson(windString, Wind::class.java)
    }

    @TypeConverter
    fun fromRain(rain: Rain?): String {
        return Gson().toJson(rain)
    }

    @TypeConverter
    fun toRain(rainString: String): Rain? {
        return Gson().fromJson(rainString, Rain::class.java)
    }

    @TypeConverter
    fun fromClouds(clouds: Clouds): String {
        return Gson().toJson(clouds)
    }

    @TypeConverter
    fun toClouds(cloudString: String): Clouds {
        return Gson().fromJson(cloudString, Clouds::class.java)
    }

    @TypeConverter
    fun fromSys(sys: Sys): String {
        return Gson().toJson(sys)
    }

    @TypeConverter
    fun toSys(sysString: String): Sys {
        return Gson().fromJson(sysString, Sys::class.java)
    }

    @TypeConverter
    fun fromHourlyForecastList(hourlyForecastList: List<HourlyForecast>): String {
        return Gson().toJson(hourlyForecastList)
    }

    @TypeConverter
    fun toHourlyForecastList(hourlyForecastListString: String): List<HourlyForecast> {
        val type = object : TypeToken<List<HourlyForecast>>() {}.type
        return Gson().fromJson(hourlyForecastListString, type)
    }

    @TypeConverter
    fun fromDailyForecastList(dailyForecastList: List<DailyForecast>): String {
        return Gson().toJson(dailyForecastList)
    }

    @TypeConverter
    fun toDailyForecastList(dailyForecastListString: String): List<DailyForecast> {
        val type = object : TypeToken<List<DailyForecast>>() {}.type
        return Gson().fromJson(dailyForecastListString, type)
    }

    @TypeConverter
    fun fromCity(city: City): String {
        return Gson().toJson(city)
    }

    @TypeConverter
    fun toCity(cityString: String): City {
        return Gson().fromJson(cityString, City::class.java)
    }
}