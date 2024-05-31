package ipn.escom.meteora.data.weather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ipn.escom.meteora.data.weather.data.local.converters.Converters
import ipn.escom.meteora.data.weather.data.local.entities.DailyForecastEntity
import ipn.escom.meteora.data.weather.data.local.entities.HourlyForecastEntity
import ipn.escom.meteora.data.weather.data.local.entities.WeatherEntity

@Database(entities = [WeatherEntity::class, HourlyForecastEntity::class, DailyForecastEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
