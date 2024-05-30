package ipn.escom.meteora.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.R
import ipn.escom.meteora.data.predictions.data.network.response.MonthPrediction
import ipn.escom.meteora.data.predictions.data.network.response.PredictionsResponse
import ipn.escom.meteora.utils.getMonthName

@Composable
fun PredictionsCard(predictionsResponse: PredictionsResponse) {
    val localityPredictions = predictionsResponse.predictions

    localityPredictions.forEach { prediction ->
        prediction.years.forEach { yearPrediction ->
            yearPrediction.months.forEach { monthPrediction ->
                var expanded by remember { mutableStateOf(false) }

                MonthPredictionCard(
                    monthPrediction = monthPrediction,
                    expanded = expanded,
                    onClick = { expanded = !expanded })
            }
        }
    }
}

@Composable
fun MonthPredictionCard(monthPrediction: MonthPrediction, expanded: Boolean, onClick: () -> Unit) {
    val scale: Float by animateFloatAsState(if (expanded) 0.95f else 1f, label = "")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(onClick = onClick)
            .scale(scale),
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
            val rotation: Float by animateFloatAsState(if (expanded) 180f else 0f, label = "")
            Icon(
                imageVector = Icons.Rounded.ExpandMore,
                contentDescription = "Expand button",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(0.1f)
                    .clickable {
                        onClick()
                    }
                    .rotate(rotation)
            )
        }
    }
    if (expanded) {
        val enterTransition = rememberInfiniteTransition(label = "")
        val delayOffset = enterTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, delayMillis = 300),
                repeatMode = RepeatMode.Restart
            ), label = ""
        )
        monthPrediction.days.forEachIndexed { index, dayPrediction ->
            val offset by animateDpAsState(
                targetValue = if (expanded) 0.dp else (-100).dp,
                animationSpec = tween(300, delayMillis = (50 * index).coerceAtMost(300)), label = ""
            )
            DailyPredictionCard(
                maxt = dayPrediction.prediction.maxt,
                mint = dayPrediction.prediction.mint,
                rainfall = dayPrediction.prediction.rainfall,
                day = dayPrediction.day,
                modifier = Modifier.offset(x = offset * delayOffset.value)
            )
        }
    }
}

@Composable
fun DailyPredictionCard(
    maxt: Double,
    mint: Double,
    rainfall: Double,
    day: Int,
    modifier: Modifier
) {
    Card(
        modifier = modifier
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
                imageVector = Icons.Rounded.Thermostat,
                contentDescription = "Max Temperature",
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "${String.format("%.2f", maxt)}°",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.rain),
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