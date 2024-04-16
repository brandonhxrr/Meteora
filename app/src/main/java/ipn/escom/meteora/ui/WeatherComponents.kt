package ipn.escom.meteora.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Column( modifier = Modifier.fillMaxWidth().padding(16.dp)){
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
fun WeatherParameters(feelsLike: Double, humidity: Int, windSpeed: Double) {
    val titles = listOf("Sensación térmica", "Humedad", "Velocidad del viento")
    val values = listOf("$feelsLike °C", "$humidity %", "$windSpeed km/h")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Column {
                Row {
                    titles.forEach { title ->
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f)
                                .wrapContentWidth(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Row {
                    values.forEach { value ->
                        Text(
                            text = value,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }

                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun WeatherPreview() {
    Weather(25.0, "Soleado", 25.0, 50, 10.0, "Ciudad de México")
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun WeatherParametersPreview() {
    WeatherParameters(feelsLike = 30.0, humidity = 60, windSpeed = 15.0)
}