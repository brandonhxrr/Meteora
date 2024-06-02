package ipn.escom.meteora.ui

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.R
import ipn.escom.meteora.data.events.data.network.response.EventResponse
import ipn.escom.meteora.data.localities.getLocalityNameFromKey
import ipn.escom.meteora.data.predictions.data.network.response.MonthPrediction
import ipn.escom.meteora.data.predictions.data.network.response.PredictionsResponse
import ipn.escom.meteora.ui.theme.getOnBackground
import ipn.escom.meteora.utils.getMonthName
import java.util.Calendar

@Composable
fun PredictionsCard(
    predictionsResponse: PredictionsResponse, onDayClick: (EventResponse) -> Unit
) {
    val localityPredictions = predictionsResponse.predictions

    localityPredictions.forEach { prediction ->
        val localityName = prediction.locality
        prediction.years.forEach { yearPrediction ->
            yearPrediction.months.forEach { monthPrediction ->
                var expanded by remember { mutableStateOf(false) }

                MonthPredictionCard(
                    localityName = localityName,
                    monthPrediction = monthPrediction,
                    year = yearPrediction.year,
                    expanded = expanded,
                    onClick = { expanded = !expanded },
                    onDayClick = onDayClick
                )
            }
        }
    }
}


@Composable
fun MonthPredictionCard(
    localityName: String,
    monthPrediction: MonthPrediction,
    year: Int,
    expanded: Boolean,
    onClick: () -> Unit,
    onDayClick: (EventResponse) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
            .animateContentSize(), colors = CardDefaults.cardColors(
            containerColor = getOnBackground(), contentColor = MaterialTheme.colorScheme.onSurface
        )
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
            Icon(imageVector = Icons.Rounded.ExpandMore,
                contentDescription = "Expand button",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(0.1f)
                    .clickable {
                        onClick()
                    }
                    .rotate(rotation))
        }
    }
    if (expanded) {
        val enterTransition = rememberInfiniteTransition(label = "")
        val delayOffset = enterTransition.animateFloat(
            initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
                animation = tween(1000, delayMillis = 300), repeatMode = RepeatMode.Restart
            ), label = ""
        )
        monthPrediction.days.forEachIndexed { index, dayPrediction ->
            val offset by animateDpAsState(
                targetValue = if (expanded) 0.dp else (-100).dp,
                animationSpec = tween(300, delayMillis = (50 * index).coerceAtMost(300)),
                label = ""
            )
            DailyPredictionCard(
                localityName = localityName,
                maxt = dayPrediction.prediction.maxt,
                mint = dayPrediction.prediction.mint,
                rainfall = dayPrediction.prediction.rainfall,
                day = dayPrediction.day,
                month = monthPrediction.month,
                year = year,
                modifier = Modifier.offset(x = offset * delayOffset.value),
                onClick = onDayClick
            )
        }
    }
}


@SuppressLint("DefaultLocale")
@Composable
fun DailyPredictionCard(
    localityName: String,
    maxt: Double,
    mint: Double,
    rainfall: Double,
    day: Int,
    month: Int,
    year: Int,
    modifier: Modifier,
    onClick: (EventResponse) -> Unit
) {
    val dateInMillis = Calendar.getInstance().apply {
        set(year, month - 1, day)
    }.timeInMillis

    Card(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .clickable {
                val eventResponse = EventResponse(
                    id = null,
                    title = "",
                    description = "",
                    date = dateInMillis,
                    time = 0L,
                    location = getLocalityNameFromKey(localityName)
                )
                onClick(eventResponse)
            }, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$day",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = getOnBackground()
                )
            }
            Spacer(modifier = Modifier.padding(16.dp))
            Row {
                Icon(
                    imageVector = Icons.Rounded.ExpandLess,
                    contentDescription = "Max Temperature",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${String.format("%.2f", maxt)}°",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))

            Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_rain),
                    contentDescription = "Rainfall",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${if (rainfall > 0.0) String.format("%.2f", rainfall) else "0"} mm",
                    style = MaterialTheme.typography.bodyMedium
                )

            }

            Spacer(modifier = Modifier.width(12.dp))

            Row {
                Icon(
                    imageVector = Icons.Rounded.ExpandMore,
                    contentDescription = "Min Temperature",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${String.format("%.2f", mint)}°",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }
        HorizontalDivider()
    }
}
