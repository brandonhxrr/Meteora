package ipn.escom.meteora.data.predictions.data.network.response

import android.annotation.SuppressLint

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

fun PredictionsResponse.toStringPredictionsResponse(useMetric: Boolean, showDecimals: Boolean): StringPredictionsResponse {
    val convertedPredictions = this.predictions.map { localityPrediction ->
        LocalityStringPrediction(
            locality = localityPrediction.locality,
            years = localityPrediction.years.map { yearPrediction ->
                YearStringPrediction(
                    year = yearPrediction.year,
                    months = yearPrediction.months.map { monthPrediction ->
                        MonthStringPrediction(
                            month = monthPrediction.month,
                            days = monthPrediction.days.map { dayPrediction ->
                                DayStringPrediction(
                                    day = dayPrediction.day,
                                    prediction = dayPrediction.prediction.toStringPrediction(useMetric, showDecimals)
                                )
                            }
                        )
                    }
                )
            }
        )
    }
    return StringPredictionsResponse(predictions = convertedPredictions)
}

@SuppressLint("DefaultLocale")
fun Prediction.toStringPrediction(useMetric: Boolean, showDecimals: Boolean): StringPrediction {
    return StringPrediction(
        maxt = convertUnitValue(this.maxt, useMetric, showDecimals),
        mint = convertUnitValue(this.mint, useMetric, showDecimals),
        rainfall = if(this.rainfall > 0.0) String.format("%.2f", this.rainfall) else "0"
    )
}

@SuppressLint("DefaultLocale")
fun convertUnitValue(value: Double, useMetric: Boolean, showDecimals: Boolean): String {
    val unitValue = if (useMetric) value else (value * 9/5) + 32
    return if (showDecimals) String.format("%.2f", unitValue) else unitValue.toInt().toString()
}