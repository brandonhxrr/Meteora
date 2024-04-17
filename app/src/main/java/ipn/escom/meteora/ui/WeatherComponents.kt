package ipn.escom.meteora.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.ui.theme.amber

@Composable
fun Weather(
    temperature: Double,
    description: String,
    feelsLike: Double,
    humidity: Int,
    windSpeed: Double,
    name: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.WbSunny,
            contentDescription = "Soleado",
            tint = amber,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = "$temperature °C",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun WeatherParameter(weatherObject: WeatherObject, modifier: Modifier) {
    Card(modifier = modifier.padding(8.dp)) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = weatherObject.icon,
                contentDescription = weatherObject.contentDescription,
                tint = weatherObject.iconColor,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = weatherObject.value,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.wrapContentWidth()
            )
        }
    }
}

@Composable
fun WeatherParameters(weatherObjects: List<WeatherObject>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        for(i in weatherObjects.indices step 2) {
            Row(modifier = Modifier.fillMaxWidth()) {
                WeatherParameter(weatherObject = weatherObjects[i], modifier = Modifier.weight(1f))
                WeatherParameter(weatherObject = weatherObjects[i+1], modifier = Modifier.weight(1f))
            }
        }

    }
}

data class WeatherObject(
    val value: String,
    val icon: ImageVector,
    val contentDescription: String,
    val iconColor: Color
)

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun WeatherPreview() {
    Weather(25.0, "Soleado", 25.0, 50, 10.0, "Ciudad de México")
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun WeatherParametersPreview() {
    val weatherObjects = listOf(
        WeatherObject("25 °C", Icons.Rounded.WbSunny, "Soleado", amber),
        WeatherObject("25 km/h", Icons.Rounded.Air, "Viento", Color.Blue),
        WeatherObject("50%", Icons.Rounded.Air, "Humedad", Color.Green),
        WeatherObject("25 °C", Icons.Rounded.WbSunny, "Soleado", amber),
    )
    WeatherParameters(weatherObjects)
}