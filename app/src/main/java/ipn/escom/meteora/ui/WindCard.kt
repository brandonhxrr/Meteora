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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.R

@Composable
fun WindCardContent(
    modifier: Modifier = Modifier,
    windSpeed: Double,
    windDirection: Int,
    hiddenIcon: Boolean = false,
) {
    ParameterCard(title = stringResource(id = R.string.wind), modifier = modifier) {
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
                    windSpeed < 1 -> stringResource(R.string.calm)
                    windSpeed < 5 -> stringResource(R.string.low)
                    windSpeed < 11 -> stringResource(id = R.string.moderate)
                    windSpeed < 19 -> stringResource(id = R.string.strong)
                    windSpeed < 28 -> stringResource(id = R.string.very_strong)
                    else -> stringResource(id = R.string.hurricane)
                }

                Text(
                    text = "$intensity · ${getWindDirection(windDirection)}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                )
            }
            if (!hiddenIcon) {
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
            contentDescription = stringResource(id = R.string.wind_direction),
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

@Composable
fun getWindDirection(windDirection: Int): String {
    return when {
        windDirection < 22 -> stringResource(R.string.north)
        windDirection < 67 -> stringResource(R.string.northeast)
        windDirection < 112 -> stringResource(R.string.east)
        windDirection < 157 -> stringResource(R.string.southeast)
        windDirection < 202 -> stringResource(R.string.south)
        windDirection < 247 -> stringResource(R.string.southwest)
        windDirection < 292 -> stringResource(R.string.west)
        windDirection < 337 -> stringResource(R.string.northwest)
        else -> stringResource(R.string.north)
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