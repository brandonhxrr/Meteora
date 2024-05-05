package ipn.escom.meteora.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Thermostat
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.data.predictions.data.network.response.MonthPrediction
import ipn.escom.meteora.data.predictions.data.network.response.PredictionsResponse
import ipn.escom.meteora.utils.getMonthName

@Composable
fun PredictionsCard(predictionsResponse: PredictionsResponse) {
    val localityPredictions = predictionsResponse.predictions
    val gustavoPredictions = localityPredictions.find { it.locality == "GUSTAVO A MADERO" }

    Log.d("PredictionsCard", "Predictions: $gustavoPredictions")

    gustavoPredictions?.years?.forEach { yearPrediction ->
        yearPrediction.months.forEach { monthPrediction ->
            var expanded by remember { mutableStateOf(false) }

            MonthPredictionCard(
                monthPrediction = monthPrediction,
                expanded = expanded,
                onClick = { expanded = !expanded })

        }
    }
}

@Composable
fun MonthPredictionCard(monthPrediction: MonthPrediction, expanded: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = getMonthName(monthNumber = monthPrediction.month),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
            )
            Icon(
                imageVector = if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                contentDescription = "Expand button",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(0.1f)
                    .clickable {
                        onClick()
                    }
            )
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

        },
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.38f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$day",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Icon(
                imageVector = Icons.Rounded.Thermostat, // Replace with your icon
                contentDescription = "Max Temperature",
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "${String.format("%.2f", maxt)}°",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Icon(
                imageVector = Icons.Rounded.WaterDrop, // Replace with your icon
                contentDescription = "Rainfall",
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "${if (rainfall > 0.0) String.format("%.2f", rainfall) else "0"} mm",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Icon(
                imageVector = Icons.Rounded.Thermostat, // Replace with your icon
                contentDescription = "Min Temperature",
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "${String.format("%.2f", mint)}°",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        HorizontalDivider()
    }
}