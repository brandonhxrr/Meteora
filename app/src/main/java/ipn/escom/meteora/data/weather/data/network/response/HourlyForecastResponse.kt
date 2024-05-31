package ipn.escom.meteora.data.weather.data.network.response

data class HourlyForecastResponse(
    val cod: String = "",
    val message: Double = 0.0,
    val cnt: Int = 0,
    val list: List<HourlyForecast> = listOf(HourlyForecast()),
    val city: City = City()
)

data class HourlyForecast(
    val dt: Long = 0L,
    val main: Main = Main(),
    val weather: List<WeatherDetail> = listOf(WeatherDetail()),
    val clouds: Clouds = Clouds(),
    val wind: Wind = Wind(),
    val visibility: Int = 0,
    val pop: Double = 0.0,
    val rain: Rain? = Rain(),
    val sys: Sys = Sys(),
    val dt_txt: String = ""
)

data class City(
    val id: Int = 0,
    val name: String = "",
    val coord: Coord = Coord(),
    val country: String = "",
    val population: Int = 0,
    val timezone: Int = 0,
    val sunrise: Long = 0L,
    val sunset: Long = 0L
)