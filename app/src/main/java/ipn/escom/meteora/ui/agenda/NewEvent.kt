package ipn.escom.meteora.ui.agenda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ipn.escom.meteora.data.events.EventViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEventBottomSheet(
    eventViewModel: EventViewModel,
    sheetState: SheetState = rememberModalBottomSheetState(),
    scope: CoroutineScope,
    onDismissRequest: () -> Unit
) {

    val eventName: String by eventViewModel.eventName.observeAsState("")
    val eventDescription by eventViewModel.eventDescription.observeAsState("")
    val eventTime by eventViewModel.eventTime.observeAsState(0L)
    val eventDate by eventViewModel.eventDate.observeAsState(0L)
    val eventLocation by eventViewModel.eventLocation.observeAsState("")
    var showDialog by remember { mutableStateOf(false) }
    var allDayEvent by remember {
        mutableStateOf(false)
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest, sheetState = sheetState
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                showDialog = true
            }) {
                Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close")
            }
            Button(onClick = { /*TODO*/ }, modifier = Modifier.padding(horizontal = 16.dp)) {
                Text("Guardar")
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                TextField(
                    value = eventName,
                    onValueChange = { eventViewModel.onEventNameChanged(it) },
                    placeholder = {
                        Text(
                            "Agregar título",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 24.sp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 24.sp),
                    maxLines = 1,
                    singleLine = true

                )
                HorizontalDivider()
                ListSelector()
                HorizontalDivider()

                EventDateTimePicker(allDayEvent = allDayEvent,
                    onAllDayEventChanged = { allDayEvent = !allDayEvent },
                    date = eventDate,
                    time = eventTime,
                    onDateSelected = { date -> eventViewModel.onEventDateChanged(date) },
                    onTimeSelected = { time -> eventViewModel.onEventTimeChanged(time) })


                HorizontalDivider()

                DropdownMenuLocation(selectedLocation = eventLocation,
                    onLocationSelected = { location ->
                        eventViewModel.onEventLocationChanged(
                            location
                        )
                    })

                HorizontalDivider()


                DescriptionField(eventDescription = eventDescription,
                    onEventDescriptionChanged = { description ->
                        eventViewModel.onEventDescriptionChanged(
                            description
                        )
                    })
            }

        }
        if (showDialog) {
            AlertDialog(onDismissRequest = { showDialog = false },
                title = { Text("Cancelar cambios") },
                text = { Text("¿Está seguro de que desea cancelar los cambios?") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onDismissRequest()
                            }
                        }

                    }) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("No")
                    }
                })
        }
    }
}