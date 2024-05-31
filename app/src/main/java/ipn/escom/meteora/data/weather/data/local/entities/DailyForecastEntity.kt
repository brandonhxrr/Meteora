package ipn.escom.meteora.data.weather.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ipn.escom.meteora.data.weather.data.local.converters.Converters
import ipn.escom.meteora.data.weather.data.network.response.City
import ipn.escom.meteora.data.weather.data.network.response.DailyForecast

@Entity(tableName = "daily_forecast")
@TypeConverters(Converters::class)
data class DailyForecastEntity(
    @PrimaryKey val id: Int = 0,
    val cod: String = "",
    val message: Double = 0.0,
    val cnt: Int = 0,
    val list: List<DailyForecast> = listOf(),
    val city: City = City()
)