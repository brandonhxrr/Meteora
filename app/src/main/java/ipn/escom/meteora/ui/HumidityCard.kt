package ipn.escom.meteora.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.ui.theme.amber
import ipn.escom.meteora.ui.theme.amberLight

@Composable
fun HumidityCard(humity: Int, modifier: Modifier = Modifier) {
    ParameterCard(title = "Humedad", modifier = modifier) {
        Row {
            Text(
                text = "$humity%",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(32.dp))
            HumidityVisualizer(humity)
        }
    }

}

@Composable
fun HumidityVisualizer(humidity: Int) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {

        Text(
            text = "100",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f)
                .padding(bottom = 12.dp)
        )
        LinearProgressIndicator(
            progress = { humidity / 100f },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(40.dp)
                .height(24.dp)
                .rotate(-90f)
                .clip(shape = RoundedCornerShape(20.dp)),
            color = amber,
            trackColor = amberLight
        )

        Text(
            text = "0",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 12.dp)
        )
    }
}


@Preview
@Composable
fun PreviewHumityCard() {
    HumidityCard(
        50, modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
    )
}