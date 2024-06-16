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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.R
import ipn.escom.meteora.utils.formatSelectedDate
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(date: Long, enabled: Boolean, onDateSelected: (Long) -> Unit) {
    var initialSelectedDateMillis by remember {
        mutableLongStateOf(date.takeIf { it != 0L } ?: System.currentTimeMillis())
    }

    if (date == 0L) {
        initialSelectedDateMillis = System.currentTimeMillis()
        onDateSelected(initialSelectedDateMillis)
    }

    var isDatePickerOpen by remember { mutableStateOf(false) }

    val pickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val selectedDateUtc = Instant.ofEpochMilli(utcTimeMillis)
                    .atZone(ZoneOffset.UTC)
                    .toLocalDate()
                val todayUtc = LocalDate.now(ZoneOffset.UTC)
                val sixMonthsLater = todayUtc.plusMonths(6)

                return (selectedDateUtc.isEqual(todayUtc) || selectedDateUtc.isAfter(todayUtc)) &&
                        !selectedDateUtc.isAfter(sixMonthsLater)
            }
        }
    )

    Box(
        modifier = Modifier
            .clickable { if (enabled) isDatePickerOpen = true }
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = formatSelectedDate(date),
            style = MaterialTheme.typography.bodyMedium
        )
    }

    if (isDatePickerOpen) {
        DatePickerDialog(
            onDismissRequest = { isDatePickerOpen = false },
            confirmButton = {
                TextButton(onClick = {
                    isDatePickerOpen = false
                    val selectedDateMillis = pickerState.selectedDateMillis ?: return@TextButton
                    onDateSelected(selectedDateMillis)
                    initialSelectedDateMillis = selectedDateMillis
                }) {
                    Text(stringResource(R.string.ok))
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
