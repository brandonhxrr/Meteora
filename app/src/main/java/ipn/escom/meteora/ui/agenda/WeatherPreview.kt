package ipn.escom.meteora.ui.agenda

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.R
import ipn.escom.meteora.data.predictions.data.network.response.Prediction

@SuppressLint("DefaultLocale")
@Composable
fun WeatherPreview(
    prediction: Prediction
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowDropUp,
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .size(24.dp)
                .align(Alignment.Top)
        )
        Text(
            text = "Temperatura máxima:",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp)
        )
        Text(
            text = "${
                String.format(
                    "%.2f",
                    prediction.maxt
                )
            } °",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium
        )

    }

    HorizontalDivider()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.rain),
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .size(24.dp)
                .align(Alignment.Top)
        )
        Text(
            text = "Nivel de lluvia:",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp)
        )
        Text(
            text = "${
                if (prediction.rainfall > 0.0) String.format(
                    "%.2f",
                    prediction.rainfall
                ) else "0"
            } mm",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    HorizontalDivider()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowDropDown,
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .size(24.dp)
                .align(Alignment.Top)
        )
        Text(
            text = "Temperatura mínima:",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp)
        )
        Text(
            text = "${
                String.format(
                    "%.2f",
                    prediction.mint
                )
            } °",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherPreviewPreview() {
    Column {
        WeatherPreview(
            prediction = Prediction(
                maxt = 30.0,
                mint = 20.0,
                rainfall = 0.0
            )
        )
    }
}