package ipn.escom.meteora.ui.agenda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EventDateTimePicker(
    allDayEvent: Boolean,
    onAllDayEventChanged: (Boolean) -> Unit,
    date: Long,
    time: Long,
    enabled: Boolean = true,
    onDateSelected: (Long) -> Unit,
    onTimeSelected: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.AccessTime,
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .size(24.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Todo el día",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Switch(checked = allDayEvent, onCheckedChange = onAllDayEventChanged)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DatePickerField(
                    date = date,
                    enabled = enabled,
                    onDateSelected = { onDateSelected(it) })
                if (!allDayEvent) {
                    TimePickerField(
                        time = time,
                        enabled = enabled,
                        onTimeSelected = { onTimeSelected(it) })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventDateTimePickerPreview() {
    EventDateTimePicker(allDayEvent = false,
        onAllDayEventChanged = {},
        date = 0L,
        time = 0L,
        onDateSelected = {},
        onTimeSelected = {})
}