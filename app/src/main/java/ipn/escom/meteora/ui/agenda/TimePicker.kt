package ipn.escom.meteora.ui.agenda

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ipn.escom.meteora.utils.convertMillisToTimeFormat
import ipn.escom.meteora.utils.getHourWithMinutesString
import ipn.escom.meteora.utils.getHoursAndMinutesFromMillis
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.Calendar

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerField(time: Long, onTimeSelected: (Long) -> Unit) {
    var initialSelectedTimeMillis by remember { mutableLongStateOf(time) }

    if(time == 0L){
        initialSelectedTimeMillis = System.currentTimeMillis()
        onTimeSelected(initialSelectedTimeMillis)
    }

    var isTimePickerOpen by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .clickable { isTimePickerOpen = true }
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = convertMillisToTimeFormat(time),
            style = MaterialTheme.typography.bodyMedium
        )
    }

    if (isTimePickerOpen) {
        val timePickerState = remember {
            TimePickerState(
                initialHour = getHoursAndMinutesFromMillis(time).first,
                initialMinute = getHoursAndMinutesFromMillis(time).second,
                is24Hour = false
            )
        }

        Dialog(onDismissRequest = { isTimePickerOpen = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TimePicker(
                        state = timePickerState,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { isTimePickerOpen = false }) {
                            Text("Cancelar")
                        }
                        TextButton(onClick = {
                            val selectedLocalTime = LocalTime.of(
                                timePickerState.hour,
                                timePickerState.minute
                            )
                            val selectedTimeMillis = selectedLocalTime.toNanoOfDay() / 1_000_000
                            onTimeSelected(selectedTimeMillis)
                            isTimePickerOpen = false
                        }) {
                            Text("Aceptar")
                        }
                    }
                }
            }
        }
    }
}
