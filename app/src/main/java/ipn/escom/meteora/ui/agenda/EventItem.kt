package ipn.escom.meteora.ui.agenda

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.data.events.data.network.response.EventResponse
import ipn.escom.meteora.utils.convertMillisToTimeFormat
import ipn.escom.meteora.utils.formatSelectedDate

@Composable
fun EventItem(eventResponse: EventResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable {
                Log.d("EventItem", "${eventResponse.id}")
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = formatSelectedDate(eventResponse.date, "dd"),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = formatSelectedDate(eventResponse.date, "MMM"),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {

                Text(
                    text = eventResponse.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (eventResponse.time != 0L) {
                    EventItemParameter(
                        icon = Icons.Outlined.WatchLater,
                        text = convertMillisToTimeFormat(eventResponse.time)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                EventItemParameter(
                    icon = Icons.Outlined.LocationOn,
                    text = eventResponse.location
                )
            }
        }
    }
}

@Composable
fun EventItemParameter(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EventItemPreview() {
    EventItem(
        EventResponse(
            id = "1",
            title = "Evento de prueba",
            description = "Descripción de prueba",
            location = "Lugar de prueba",
            date = 1683993600000L, // 13 May 2023 00:00:00 GMT
            time = 3600000L // 1 hour in milliseconds
        )
    )
}
