package ipn.escom.meteora.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import ipn.escom.meteora.R
import ipn.escom.meteora.data.weather.data.network.response.formatTemperature

@Composable
fun CurrentWeatherContent(
    modifier: Modifier = Modifier,
    showDecimals: Boolean = false,
    location: String,
    time: String,
    temperature: Double,
    feelsLike: Double,
    description: Int,
    maxt: Double? = null,
    mint: Double? = null,
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
                    text = formatTemperature(temperature, showDecimals),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 45.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = stringResource(
                        R.string.feels_like_value,
                        formatTemperature(feelsLike, showDecimals)
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
                if (maxt != null && mint != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowDropUp,
                                contentDescription = stringResource(R.string.maximum),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = formatTemperature(maxt, showDecimals),
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                        Row(
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowDropDown,
                                contentDescription = stringResource(R.string.minimum),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = formatTemperature(mint, showDecimals),
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }

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
            animatedIcon = R.raw.weather_sunny
        )
    }
}