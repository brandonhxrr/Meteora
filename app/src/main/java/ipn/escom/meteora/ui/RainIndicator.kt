package ipn.escom.meteora.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RainIndicator(
    modifier: Modifier = Modifier,
    rain: Double? = null,
    pop: Double,
    hiddenIcon: Boolean = false
) {
    ParameterCard(title = "Lluvia", modifier = modifier) {
        Row(modifier = Modifier.fillMaxHeight()) {
            Column(
                modifier = Modifier.align(Alignment.CenterVertically),
            ) {
                Text(
                    text = "${(pop * 100).toInt()}%",
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )
                if (rain != null) {
                    Text(text = "$rain mm", style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            if (!hiddenIcon) {
                RainVisualizer(pop)
            }
        }

    }
}


@Composable
fun RainVisualizer(rain: Double) {
    val animatedProgress by animateFloatAsState(targetValue = (rain).toFloat(), label = "")

    CircularProgressIndicator(
        progress = { animatedProgress },
        modifier = Modifier.size(100.dp),
        strokeCap = StrokeCap.Round,
        strokeWidth = 12.dp,
        color = MaterialTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
    )
}

@Preview(showBackground = true)
@Composable
fun RainIndicatorPreview() {
    RainIndicator(pop = 0.5)
}
