package ipn.escom.meteora.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import ipn.escom.meteora.data.weather.data.network.response.HourlyForecast
import ipn.escom.meteora.data.weather.data.network.response.HourlyForecastResponse
import ipn.escom.meteora.data.weather.getAnimatedIcon
import ipn.escom.meteora.data.weather.getDescription
import ipn.escom.meteora.utils.getDateString
import ipn.escom.meteora.utils.getHourWithMinutesString

@Composable
fun HourlyWeather(hourlyForecastResponse: HourlyForecastResponse? = null) {

    var showDialog by remember { mutableStateOf(false) }
    var selectedForecast by remember { mutableStateOf<HourlyForecast?>(null) }

    LazyRow(modifier = Modifier.padding(horizontal = 16.dp)) {
        if (hourlyForecastResponse != null) {
            items(24) { index ->
                HourlyWeatherCard(hourlyForecastResponse.list[index]) {
                    selectedForecast = hourlyForecastResponse.list[index]
                    showDialog = true
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Column {
                    Text(
                        text = getDateString(selectedForecast?.dt ?: 0),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = getHourWithMinutesString(selectedForecast?.dt ?: 0),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                    )
                }
            },
            text = {
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(
                        getAnimatedIcon(selectedForecast?.weather?.get(0)?.icon ?: "")
                    )
                )
                Column {
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = stringResource(
                            id = getDescription(
                                selectedForecast?.weather?.get(0)?.icon ?: ""
                            )
                        ),
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(140.dp)
                    ) {
                        WindCardContent(
                            windSpeed = selectedForecast?.wind?.speed ?: 0.0,
                            windDirection = selectedForecast?.wind?.deg ?: 0,
                            hiddenIcon = true,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        HumidityCard(
                            humity = selectedForecast?.main?.humidity ?: 0,
                            hiddenIcon = true,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

}

@Composable
fun HourlyWeatherCard(hourlyForecast: HourlyForecast, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(180.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(getAnimatedIcon(hourlyForecast.weather[0].icon))
            )
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.CenterHorizontally)
                    .size(48.dp),
            )
            Text(
                text = getHourWithMinutesString(hourlyForecast.dt), modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 10.dp), style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = hourlyForecast.main.temp.toString() + " °C", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 4.dp), style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DailyWeatherPreview() {
    HourlyWeather()
}