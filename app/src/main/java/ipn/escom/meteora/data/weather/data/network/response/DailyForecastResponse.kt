package ipn.escom.meteora.data.weather.data.network.response

data class DailyForecastResponse(
    val cod: String = "",
    val message: Double = 0.0,
    val cnt: Int = 0,
    val list: List<DailyForecast> = listOf(DailyForecast()),
    val city: City = City()
)

data class DailyForecast(
    val dt: Long = 0L,
    val sunrise: Long = 0L,
    val sunset: Long = 0L,
    val temp: Temp = Temp(),
    val feels_like: FeelsLike = FeelsLike(),
    val pressure: Int = 0,
    val humidity: Int = 0,
    val weather: List<WeatherDetail> = listOf(WeatherDetail()),
    val speed: Double = 0.0,
    val deg: Int = 0,
    val clouds: Int = 0,
    val pop: Double = 0.0,
    val rain: Double? = 0.0
)

data class Temp(
    val day: Double = 0.0,
    val min: Double = 0.0,
    val max: Double = 0.0,
    val night: Double = 0.0,
    val eve: Double = 0.0,
    val morn: Double = 0.0
)

data class FeelsLike(
    val day: Double = 0.0,
    val night: Double = 0.0,
    val eve: Double = 0.0,
    val morn: Double = 0.0
)