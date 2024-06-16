package ipn.escom.meteora.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import ipn.escom.meteora.R
import ipn.escom.meteora.data.weather.data.network.response.HourlyForecast
import ipn.escom.meteora.data.weather.data.network.response.formatTemperature
import ipn.escom.meteora.data.weather.getAnimatedIcon
import ipn.escom.meteora.data.weather.getDescription
import ipn.escom.meteora.ui.theme.getOnBackground
import ipn.escom.meteora.utils.getDayOfWeekFromLong
import ipn.escom.meteora.utils.getHourWithMinutesString
import ipn.escom.meteora.utils.getOnlyDateString

@Composable
fun HourlyWeather(showDecimals: Boolean = false, hourlyForecastList: List<HourlyForecast>?) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedForecast by remember { mutableStateOf<HourlyForecast?>(null) }

    LazyRow(contentPadding = PaddingValues(start = 16.dp, end = 16.dp)) {
        if (!hourlyForecastList.isNullOrEmpty()) {
            items(hourlyForecastList.size) { index ->
                HourlyWeatherCard(showDecimals, hourlyForecastList[index]) {
                    selectedForecast = hourlyForecastList[index]
                    showDialog = true
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }

    HourlyForecastDialog(showDialog, { showDialog = false }, selectedForecast)
}


@Composable
fun HourlyWeatherCard(
    showDecimals: Boolean = false,
    hourlyForecast: HourlyForecast,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(90.dp)
            .height(150.dp)
            .clickable(onClick = onClick),
        colors = CardColors(
            containerColor = getOnBackground(),
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.38f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(getAnimatedIcon(hourlyForecast.weather[0].icon))
            )
            Text(
                text = formatTemperature(hourlyForecast.main.temp, showDecimals),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 4.dp),
                style = MaterialTheme.typography.bodyMedium
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
                    .padding(vertical = 10.dp), style = MaterialTheme.typography.bodyMedium
            )
            if (hourlyForecast.pop > 0.0) {
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_rain),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${(hourlyForecast.pop * 100).toInt()}%",
                        modifier = Modifier.align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun HourlyForecastDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    selectedForecast: HourlyForecast?
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(
                        getAnimatedIcon(selectedForecast?.weather?.get(0)?.icon ?: "")
                    )
                )
                Column {
                    IconButton(
                        onClick = { onDismiss() },
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.End)
                    ) {
                        Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close")
                    }
                    Row(
                        modifier = Modifier.padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = getHourWithMinutesString(selectedForecast?.dt ?: 0),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                fontSize = 32.sp
                            )
                            Text(
                                text = getDayOfWeekFromLong(selectedForecast?.dt ?: 0),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Text(
                                text = getOnlyDateString(selectedForecast?.dt ?: 0),
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
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
                        }
                    }
                }

            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    DialogParameter(
                        stringResource(R.string.temperature),
                        "${selectedForecast?.main?.temp}°"
                    )
                    DialogParameter(
                        stringResource(R.string.feels_like),
                        "${selectedForecast?.main?.feelsLike}°"
                    )
                    DialogParameter(
                        stringResource(R.string.humidity),
                        "${selectedForecast?.main?.humidity}%"
                    )
                    DialogParameter(
                        stringResource(R.string.pressure),
                        "${selectedForecast?.main?.pressure} hPa"
                    )
                    DialogParameter(
                        stringResource(R.string.wind_speed),
                        "${selectedForecast?.wind?.speed} m/s"
                    )
                    DialogParameter(
                        stringResource(R.string.wind_direction),
                        getWindDirection(selectedForecast?.wind?.deg ?: 0)
                    )
                    DialogParameter(
                        title = stringResource(R.string.probability_of_rain),
                        value = "${(selectedForecast?.pop?.times(100))?.toInt()}%"
                    )
                    DialogParameter(
                        title = stringResource(R.string.rainfall_mm),
                        value = "${selectedForecast?.rain?.oneHour ?: 0} mm"
                    )
                }
            },
            confirmButton = {

            }
        )
    }
}

@Composable
fun DialogParameter(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.4f)
        )
    }
}