package ipn.escom.meteora.ui.agenda

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(date: String, onDateSelected: (Date) -> Unit) {
    var dateText by remember { mutableStateOf(date) }
    var isDatePickerOpen by remember { mutableStateOf(false) }

    val currentDate = LocalDateTime.now()
    var selectedDate by remember { mutableStateOf(currentDate) }
    val maxDate = currentDate.plusMonths(6)

    val pickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val currentSelectedDate = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(utcTimeMillis),
                    ZoneId.systemDefault()
                )
                return !currentSelectedDate.isBefore(currentDate) && !currentSelectedDate.isAfter(
                    maxDate
                )
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isDatePickerOpen = true }
            .padding(8.dp)
    ) {
        Text(
            text = dateText.ifEmpty { "Fecha del Evento" },
            style = MaterialTheme.typography.bodyLarge
        )
    }

    if (isDatePickerOpen) {
        DatePickerDialog(
            onDismissRequest = { isDatePickerOpen = false },
            confirmButton = {
                TextButton(onClick = {
                    isDatePickerOpen = false
                    val selectedDayMillis = pickerState.selectedDateMillis
                    selectedDate =
                        Instant.ofEpochMilli(selectedDayMillis!!).atZone(ZoneId.of("UTC"))
                            .toLocalDate().atStartOfDay()
                    dateText = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    onDateSelected(
                        Date.from(selectedDate.atZone(ZoneId.systemDefault()).toInstant())
                    )
                }) {
                    Text("Aceptar")
                }
            }
        ) {
            DatePicker(
                state = pickerState,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}