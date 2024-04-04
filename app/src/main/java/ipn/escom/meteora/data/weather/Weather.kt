package ipn.escom.meteora.data.weather

data class Weather(
    val main: Main,
    val weather: List<WeatherDetail>,
    val wind: Wind,
    val name: String
)

data class Main(
    val temp: Double,
    val feelsLike: Double,
    val humidity: Int
)

data class WeatherDetail(
    val description: String
)

data class Wind(
    val speed: Double
)
