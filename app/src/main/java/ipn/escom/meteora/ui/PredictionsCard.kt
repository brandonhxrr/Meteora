package ipn.escom.meteora.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.data.predictions.data.network.response.MonthPrediction
import ipn.escom.meteora.data.predictions.data.network.response.PredictionsResponse

@Composable
fun PredictionsCard(predictionsResponse: PredictionsResponse) {
    val localityPredictions = predictionsResponse.predictions
    val gustavoPredictions = localityPredictions.find { it.locality == "GUSTAVO A MADERO" }

    Log.d("PredictionsCard", "Predictions: $gustavoPredictions")

    gustavoPredictions?.years?.forEach { yearPrediction ->
        yearPrediction.months.forEach { monthPrediction ->
            var expanded by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Year: ${yearPrediction.year}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    MonthPredictionCard(
                        monthPrediction = monthPrediction,
                        expanded = expanded,
                        onClick = { expanded = !expanded })
                }
            }
        }
    }
}

@Composable
fun MonthPredictionCard(monthPrediction: MonthPrediction, expanded: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column {
            Text(
                text = "Month: ${monthPrediction.month}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(8.dp))
        }
    }
    if (expanded) {
        monthPrediction.days.forEach { dayPrediction ->
            DailyPredictionCard(
                maxt = dayPrediction.prediction.maxt,
                mint = dayPrediction.prediction.mint,
                rainfall = dayPrediction.prediction.rainfall,
                day = dayPrediction.day
            )
        }
    }
}

@Composable
fun DailyPredictionCard(maxt: Double, mint: Double, rainfall: Double, day: Int) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable {

        }) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column {
                Text(
                    text = "$day",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$rainfall mm",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "$maxt °C / $mint °C",
                modifier = Modifier.align(Alignment.CenterVertically),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
        }


    }
}