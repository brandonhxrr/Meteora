package ipn.escom.meteora.data.predictions.data.network.response

data class StringPrediction(
    val maxt: String = "",
    val mint: String = "",
    val rainfall: String = ""
)

data class DayStringPrediction(
    val day: Int = 0,
    val prediction: StringPrediction = StringPrediction()
)

data class MonthStringPrediction(
    val month: Int = 0,
    val days: List<DayStringPrediction> = emptyList()
)

data class YearStringPrediction(
    val year: Int = 0,
    val months: List<MonthStringPrediction> = emptyList()
)

data class LocalityStringPrediction(
    val locality: String = "",
    val years: List<YearStringPrediction> = emptyList()
)

data class StringPredictionsResponse(
    val predictions: List<LocalityStringPrediction> = emptyList()
)