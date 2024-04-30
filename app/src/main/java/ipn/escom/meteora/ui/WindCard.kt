package ipn.escom.meteora.ui

import android.content.res.Configuration
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.R

@Composable
fun WindCardContent(
    windSpeed: Double,
    windDirection: Int,
    hiddenIcon: Boolean = false,
    modifier: Modifier = Modifier
) {
    ParameterCard(title = "Viento", modifier = modifier) {
        Row(modifier = modifier.fillMaxHeight()) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(70.dp)
            ) {
                Row {
                    Text(
                        text = windSpeed.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.Bottom)
                    )
                    Text(
                        text = " km/h",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.Bottom)
                    )
                }
                val intensity = when {
                    windSpeed < 1 -> "Calmado"
                    windSpeed < 5 -> "Bajo"
                    windSpeed < 11 -> "Moderado"
                    windSpeed < 19 -> "Fuerte"
                    windSpeed < 28 -> "Muy fuerte"
                    else -> "Huracanado"
                }
                val windOrigin = when {
                    windDirection < 22 -> "Norte"
                    windDirection < 67 -> "Noreste"
                    windDirection < 112 -> "Este"
                    windDirection < 157 -> "Sureste"
                    windDirection < 202 -> "Sur"
                    windDirection < 247 -> "Suroeste"
                    windDirection < 292 -> "Oeste"
                    windDirection < 337 -> "Noroeste"
                    else -> "Norte"
                }
                Text(
                    text = "$intensity · $windOrigin",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                )
            }
            if(!hiddenIcon){
                WindDirectionsVisualizer(
                    windDirection,
                    Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
fun WindDirectionsVisualizer(
    windDirection: Int,
    modifier: Modifier = Modifier
) {
    var animate by remember { mutableStateOf(false) }
    val arrowAngle: Float by animateFloatAsState(
        targetValue = windDirection.toFloat(),
        animationSpec = tween(
            durationMillis = if (animate) 1000 else 0,
            easing = FastOutSlowInEasing
        ),
        label = "",
    )

    Box(
        modifier = modifier
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.arrow),
            contentDescription = "Dirección del viento",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(50.dp)
                .rotate(arrowAngle)
                .align(Alignment.Center)
        )

    }
    LaunchedEffect(true) {
        animate = true
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WindCardPreview() {
    MaterialTheme {
        WindCardContent(
            windSpeed = 10.0,
            windDirection = 251
        )
    }
}