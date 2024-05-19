package ipn.escom.meteora.ui.agenda

import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ipn.escom.meteora.data.events.AgendaViewModel
import ipn.escom.meteora.data.events.EventViewModel
import ipn.escom.meteora.data.events.data.network.response.EventResponse
import ipn.escom.meteora.ui.theme.green
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailBottomSheet(
    agendaViewModel: AgendaViewModel,
    eventResponse: EventResponse,
    sheetState: SheetState = rememberModalBottomSheetState(),
    scope: CoroutineScope,
    onDismissRequest: () -> Unit
) {
    val eventViewModel = EventViewModel()
    val eventName: String by eventViewModel.eventName.observeAsState("")
    val eventDescription by eventViewModel.eventDescription.observeAsState("")
    val eventTime by eventViewModel.eventTime.observeAsState(0L)
    val eventDate by eventViewModel.eventDate.observeAsState(0L)
    val userId: String = eventViewModel.userId.value!!
    val eventLocation by eventViewModel.eventLocation.observeAsState("")
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isEditable by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    eventViewModel.initializeViewModel(eventResponse)

    var allDayEvent by remember { mutableStateOf(eventTime == 0L) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect {
            when (it) {
                is FocusInteraction.Focus -> scope.launch {
                    sheetState.expand()
                }
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                if (isEditable) {
                    showDialog = true
                } else {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismissRequest()
                        }
                    }
                }
            }) {
                Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close")
            }
            Button(
                onClick = {
                    if (isEditable) {
                        val event = EventResponse(
                            id = eventResponse.id,
                            title = eventName,
                            description = eventDescription,
                            date = eventDate,
                            time = if (allDayEvent) 0L else eventTime,
                            location = eventLocation
                        )
                        agendaViewModel.updateEvent(userId, event)
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onDismissRequest()
                            }
                        }
                    }
                    isEditable = !isEditable
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isEditable) green else Color.Black,
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = if (isEditable) Icons.Rounded.Check else Icons.Rounded.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isEditable) "Guardar" else "Editar")
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                TextField(
                    value = eventName,
                    onValueChange = { if (isEditable) eventViewModel.onEventNameChanged(it) },
                    placeholder = {
                        Text(
                            "Agregar título",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 24.sp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                scope.launch { sheetState.expand() }
                            }
                        },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface, // Use the same text color
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant, // If you have leading icons
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant // If you have trailing icons
                    ),
                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 24.sp),
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    enabled = isEditable
                )
                HorizontalDivider()
                ListSelector()
                HorizontalDivider()

                EventDateTimePicker(
                    allDayEvent = allDayEvent,
                    onAllDayEventChanged = { if (isEditable) allDayEvent = !allDayEvent },
                    date = eventDate,
                    time = eventTime,
                    onDateSelected = { if (isEditable) eventViewModel.onEventDateChanged(it) },
                    onTimeSelected = { if (isEditable) eventViewModel.onEventTimeChanged(it) },
                    enabled = isEditable,
                )

                HorizontalDivider()

                DropdownMenuLocation(
                    enabled = isEditable,
                    selectedLocation = eventLocation,
                    onLocationSelected = { if (isEditable) eventViewModel.onEventLocationChanged(it) }
                )

                HorizontalDivider()

                DescriptionField(
                    enabled = isEditable,
                    eventDescription = eventDescription,
                    onEventDescriptionChanged = {
                        if (isEditable) eventViewModel.onEventDescriptionChanged(
                            it
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            showDeleteDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Eliminar")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
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
                }
            )
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Eliminar evento") },
                text = { Text("¿Está seguro de que desea eliminar el evento?") },
                confirmButton = {
                    TextButton(onClick = {
                        agendaViewModel.deleteEvent(userId, eventResponse.id!!)
                        showDeleteDialog = false
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
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("No")
                    }
                }
            )
        }
    }
}
