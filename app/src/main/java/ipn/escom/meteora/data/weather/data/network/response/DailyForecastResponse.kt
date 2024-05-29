package ipn.escom.meteora.data.weather.data.network.response

data class DailyForecastResponse(
    val cod: String,
    val message: Double,
    val cnt: Int,
    val list: List<DailyForecast>,
    val city: City
)

data class DailyForecast(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Temp,
    val feels_like: FeelsLike,
    val pressure: Int,
    val humidity: Int,
    val weather: List<WeatherDetail>,
    val speed: Double,
    val deg: Int,
    val clouds: Int,
    val pop: Double,
    val rain: Double?
)

data class Temp(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class FeelsLike(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)