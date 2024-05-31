package ipn.escom.meteora.utils

import ipn.escom.meteora.data.weather.data.local.entities.DailyForecastEntity
import ipn.escom.meteora.data.weather.data.local.entities.HourlyForecastEntity
import ipn.escom.meteora.data.weather.data.local.entities.WeatherEntity
import ipn.escom.meteora.data.weather.data.network.response.DailyForecastResponse
import ipn.escom.meteora.data.weather.data.network.response.HourlyForecastResponse
import ipn.escom.meteora.data.weather.data.network.response.Rain
import ipn.escom.meteora.data.weather.data.network.response.WeatherResponse


fun WeatherEntity.toWeatherResponse(): WeatherResponse {
    return WeatherResponse(
        coord = this.coord,
        weather = this.weather,
        base = this.base,
        main = this.main,
        visibility = this.visibility,
        wind = this.wind,
        rain = this.rain ?: Rain(),
        clouds = this.clouds,
        dt = this.dt,
        sys = this.sys,
        timezone = this.timezone,
        name = this.name,
        cod = this.cod
    )
}

fun HourlyForecastEntity.toHourlyForecastResponse(): HourlyForecastResponse {
    return HourlyForecastResponse(
        cod = this.cod,
        message = this.message,
        cnt = this.cnt,
        list = this.list,
        city = this.city
    )
}

fun DailyForecastEntity.toDailyForecastResponse(): DailyForecastResponse {
    return DailyForecastResponse(
        cod = this.cod,
        message = this.message,
        cnt = this.cnt,
        list = this.list,
        city = this.city
    )
}