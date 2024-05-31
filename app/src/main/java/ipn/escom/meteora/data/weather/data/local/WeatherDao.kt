package ipn.escom.meteora.data.weather.data.local


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ipn.escom.meteora.data.weather.data.local.entities.DailyForecastEntity
import ipn.escom.meteora.data.weather.data.local.entities.HourlyForecastEntity
import ipn.escom.meteora.data.weather.data.local.entities.WeatherEntity

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("SELECT * FROM weather WHERE id = 0")
    suspend fun getWeather(): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyForecast(hourlyForecast: HourlyForecastEntity)

    @Query("SELECT * FROM hourly_forecast WHERE id = 0")
    suspend fun getHourlyForecast(): HourlyForecastEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyForecast(dailyForecast: DailyForecastEntity)

    @Query("SELECT * FROM daily_forecast WHERE id = 0")
    suspend fun getDailyForecast(): DailyForecastEntity?
}