package ipn.escom.meteora.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.shader.color
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.common.shader.DynamicShader
import com.patrykandpatrick.vico.core.common.shader.TopBottomShader
import ipn.escom.meteora.data.predictions.data.network.response.PredictionsResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun SimpleLineChart(entries: List<Pair<Float, LocalDate>>, color: Color) {
    val modelProducer = remember { CartesianChartModelProducer.build() }
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM")
    val dateLabels = remember { mutableMapOf<Float, String>() }
    val todayPosition = entries.indexOfFirst { it.second == LocalDate.now() }.toFloat()

    LaunchedEffect(entries) {
        val xValues = entries.mapIndexed { index, pair -> index.toFloat() to pair.first }.toMap()
        val formattedDates =
            entries.mapIndexed { index, pair -> index.toFloat() to pair.second.format(dateFormatter) }

        modelProducer.tryRunTransaction {
            lineSeries {
                series(xValues.values.toList())
            }
            dateLabels.clear()
            dateLabels.putAll(formattedDates)
        }
    }

    val bottomAxis = rememberBottomAxis(valueFormatter = { value, _, _ ->
        dateLabels[value] ?: value.toString()
    })

    val marker = rememberMarker()

    CartesianChartHost(chart = rememberCartesianChart(
        rememberLineCartesianLayer(
            lines = listOf(
                rememberLineSpec(
                    shader = TopBottomShader(
                        DynamicShader.color(color),
                        DynamicShader.color(Color.Transparent),
                    )
                )
            )
        ), startAxis = rememberStartAxis(), bottomAxis = bottomAxis
    ),
        modelProducer = modelProducer,
        marker = marker,
        scrollState = rememberVicoScrollState(initialScroll = { context, horizontalDimensions, _, _ ->
            val totalDays = entries.size
            val dayWidth = horizontalDimensions.getContentWidth(context) / totalDays
            todayPosition * dayWidth
        }),
        zoomState = rememberVicoZoomState(
            initialZoom = Zoom.x(6f)
        )
    )
}


fun getMaxTemperatureEntries(predictionsResponse: PredictionsResponse): List<Pair<Float, LocalDate>> {
    val entries = mutableListOf<Pair<Float, LocalDate>>()
    predictionsResponse.predictions.forEach { localityPrediction ->
        localityPrediction.years.forEach { yearPrediction ->
            yearPrediction.months.forEach { monthPrediction ->
                monthPrediction.days.forEach { dayPrediction ->
                    val predictionDate =
                        LocalDate.of(yearPrediction.year, monthPrediction.month, dayPrediction.day)
                    val yValue = dayPrediction.prediction.maxt.toFloat()
                    entries.add(yValue to predictionDate)
                }
            }
        }
    }
    return entries
}

fun getMinTemperatureEntries(predictionsResponse: PredictionsResponse): List<Pair<Float, LocalDate>> {
    val entries = mutableListOf<Pair<Float, LocalDate>>()
    predictionsResponse.predictions.forEach { localityPrediction ->
        localityPrediction.years.forEach { yearPrediction ->
            yearPrediction.months.forEach { monthPrediction ->
                monthPrediction.days.forEach { dayPrediction ->
                    val predictionDate =
                        LocalDate.of(yearPrediction.year, monthPrediction.month, dayPrediction.day)
                    val yValue = dayPrediction.prediction.mint.toFloat()
                    entries.add(yValue to predictionDate)
                }
            }
        }
    }
    return entries
}

fun getRainEntries(predictionsResponse: PredictionsResponse): List<Pair<Float, LocalDate>> {
    val entries = mutableListOf<Pair<Float, LocalDate>>()
    predictionsResponse.predictions.forEach { localityPrediction ->
        localityPrediction.years.forEach { yearPrediction ->
            yearPrediction.months.forEach { monthPrediction ->
                monthPrediction.days.forEach { dayPrediction ->
                    val predictionDate =
                        LocalDate.of(yearPrediction.year, monthPrediction.month, dayPrediction.day)
                    val yValue = dayPrediction.prediction.rainfall.toFloat()
                    entries.add(yValue to predictionDate)
                }
            }
        }
    }
    return entries
}