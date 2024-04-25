package ipn.escom.meteora.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import ipn.escom.meteora.ui.theme.amber
import ipn.escom.meteora.R

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
fun MainWeather() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Ciudad de México",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "25°",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.primary
            )
            Icon(
                imageVector = Icons.Rounded.WbSunny,
                contentDescription = "Soleado",
                modifier = Modifier.size(50.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Soleado",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.End),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Sensación térmica: 24°",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.align(Alignment.End),
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            }
        }
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
        for (i in weatherObjects.indices step 2) {
            Row(modifier = Modifier.fillMaxWidth()) {
                WeatherParameter(weatherObject = weatherObjects[i], modifier = Modifier.weight(1f))
                WeatherParameter(
                    weatherObject = weatherObjects[i + 1],
                    modifier = Modifier.weight(1f)
                )
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

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun MainWeatherPreview() {
    MainWeather()
}


@Composable
fun CurrentWeatherContent(
    modifier: Modifier = Modifier,
    location: String,
    time: String,
    temperature: Double,
    feelsLike: Double,
    description: Int,
    icon: Int,
    animatedIcon: Int
) {
    Column(
        modifier = modifier
            .padding(
                8.dp
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = location,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "$temperature °C",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 45.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = "Se siente como $feelsLike °C",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Box {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    val composition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(animatedIcon)
                            )
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = modifier.size(100.dp)
                    )
                    Text(
                        text = stringResource(id = description),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CurrentWeatherPreview() {
    MaterialTheme {
        CurrentWeatherContent(
            location = "Ciudad de Mexico",
            time = "04:56 PM",
            temperature = 25.0,
            feelsLike = 24.0,
            description = R.string.condition_sunny,
            icon = R.drawable.ic_weather_sunny,
            animatedIcon = R.raw.weather_sunny
        )
    }
}