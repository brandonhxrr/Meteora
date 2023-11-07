package ipn.escom.meteora.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.R

@Composable
fun Forecast(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.location),
                contentDescription = "Location"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Gustavo A. Madero",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Weather()
    }
}

@Composable
fun Weather() {
    Column {
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary

                )
            ) {
                Row {
                    Column(
                        modifier = Modifier.weight(0.4f)

                    ) {
                        Row(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.sun),
                                contentDescription = "Sunny",
                                modifier = Modifier.padding(top = 16.dp),
                                tint = Color.Unspecified
                            )
                        }

                        Row(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = "Sunny",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(0.6f)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "30°",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 32.dp),
                            textAlign = TextAlign.End
                        )
                    }
                }

            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ForecastPreview() {
    Forecast(modifier = Modifier)
}