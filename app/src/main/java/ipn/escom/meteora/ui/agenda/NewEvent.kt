package ipn.escom.meteora.ui.agenda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.data.events.Event
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
    val eventTime by eventViewModel.eventTime.observeAsState("")
    val eventDate by eventViewModel.eventDate.observeAsState("")
    val eventLocation by eventViewModel.eventLocation.observeAsState("")
    var showDialog by remember { mutableStateOf(false) }


    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                showDialog = true
            }) {
                Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close")
            }
            Button(onClick = { /*TODO*/ }) {
                Text("Guardar")
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = eventName,
                onValueChange = { eventViewModel.onEventNameChanged(it) },
                label = { Text("Nombre del Evento") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = eventDescription,
                onValueChange = { eventViewModel.onEventDescriptionChanged(it) },
                label = { Text("Descripción del Evento") },
                modifier = Modifier.fillMaxWidth()
            )

            TimePickerField(
                time = eventTime,
                onTimeSelected = { time -> eventViewModel.onEventTimeChanged(time) }
            )

            DatePickerField(
                date = eventDate,
                onDateSelected = { date -> eventViewModel.onEventDateChanged(date.toString()) })

            DropdownMenuLocation(selectedLocation = eventLocation,
                onLocationSelected = { location -> eventViewModel.onEventLocationChanged(location) })

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val event = Event(
                        title = eventName,
                        description = eventDescription,
                        time = eventTime,
                        date = eventDate,
                        location = eventLocation
                    )
                    //eventViewModel.addEvent(event)
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar Evento")
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


