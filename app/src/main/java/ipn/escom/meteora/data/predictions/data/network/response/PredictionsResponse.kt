package ipn.escom.meteora.data.predictions.data.network.response

data class StringPrediction(
    val maxt: String = "",
    val mint: String = "",
    val rainfall: String = ""
)

data class Prediction(
    val maxt: Double = 0.0,
    val mint: Double = 0.0,
    val rainfall: Double = 0.0
)

data class DayPrediction(
    val day: Int = 0,
    val prediction: Prediction = Prediction()
)

data class MonthPrediction(
    val month: Int = 0,
    val days: List<DayPrediction> = emptyList()
)

data class YearPrediction(
    val year: Int = 0,
    val months: List<MonthPrediction> = emptyList()
)

data class LocalityPrediction(
    val locality: String = "",
    val years: List<YearPrediction> = emptyList()
)

data class PredictionsResponse(
    val predictions: List<LocalityPrediction> = emptyList()
)
