package ipn.escom.meteora.ui.agenda

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ipn.escom.meteora.R
import ipn.escom.meteora.data.events.AgendaViewModel
import ipn.escom.meteora.data.events.EventViewModel
import ipn.escom.meteora.data.events.data.network.response.EventResponse
import ipn.escom.meteora.data.localities.getLocalityKeyFromName
import ipn.escom.meteora.data.predictions.PredictionsViewModel
import ipn.escom.meteora.data.predictions.data.network.response.Prediction
import ipn.escom.meteora.data.predictions.data.network.response.PredictionsResponse
import ipn.escom.meteora.ui.login.AlertMessage
import ipn.escom.meteora.ui.theme.green
import ipn.escom.meteora.utils.combineDateAndTime
import ipn.escom.meteora.utils.getDayFromMillis
import ipn.escom.meteora.utils.getMonthFromMillis
import ipn.escom.meteora.utils.getYearFromMillis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventBottomSheet(
    agendaViewModel: AgendaViewModel,
    eventResponse: EventResponse? = null,
    sheetState: SheetState = rememberModalBottomSheetState(),
    scope: CoroutineScope,
    onDismissRequest: () -> Unit
) {
    val eventViewModel = remember { EventViewModel() }
    val eventName: String by eventViewModel.eventName.observeAsState("")
    val eventDescription by eventViewModel.eventDescription.observeAsState("")
    val eventTime by eventViewModel.eventTime.observeAsState(0L)
    val eventDate by eventViewModel.eventDate.observeAsState(0L)
    val userId: String = eventViewModel.userId.value!!
    val eventLocation by eventViewModel.eventLocation.observeAsState("")
    var showCancelDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isEditable by remember { mutableStateOf(eventResponse == null) }
    val isNewEvent by remember { mutableStateOf(eventResponse?.title?.isEmpty() ?: true) }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    var allDayEvent by remember { mutableStateOf(eventResponse?.time == 0L) }
    val predictionsViewModel = PredictionsViewModel()
    val predictions: PredictionsResponse? by predictionsViewModel.predictions.observeAsState(initial = PredictionsResponse())
    var localityKey by remember { mutableStateOf(getLocalityKeyFromName(eventLocation)) }
    val context = LocalContext.current

    eventResponse?.let { eventViewModel.initializeViewModel(it) }

    LaunchedEffect(key1 = eventLocation) {
        if (eventLocation.isNotEmpty() && eventDate != 0L) {
            localityKey = getLocalityKeyFromName(eventLocation)
            predictionsViewModel.getPredictions(localityKey)
            Log.d("EventBottomSheet", "LocalityKey111111: $localityKey")
            Log.d("EventBottomSheet", "Predictions111111: $predictions")
        }
    }

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
                if (isEditable || isNewEvent) {
                    showCancelDialog = true
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
                    if (eventName.isBlank() || eventLocation.isBlank()) {
                        Toast.makeText(
                            context,
                            "Completa todos los campos requeridos",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (eventResponse == null || isNewEvent) {
                            val event = EventResponse(
                                title = eventName,
                                description = eventDescription,
                                date = eventDate,
                                time = if (allDayEvent) 0L else eventTime,
                                location = eventLocation
                            )
                            agendaViewModel.addEvent(userId, event)

                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    onDismissRequest()
                                }
                            }
                        } else {
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
                            } else {
                                isEditable = true
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isEditable || isNewEvent) green else Color.Black,
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = if (isEditable || isNewEvent) Icons.Rounded.Check else Icons.Rounded.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isEditable || isNewEvent) "Guardar" else "Editar")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                TextField(
                    value = eventName,
                    onValueChange = {
                        if (isEditable || isNewEvent || eventResponse == null) eventViewModel.onEventNameChanged(
                            it
                        )
                    },
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
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 24.sp),
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    enabled = isEditable || isNewEvent || eventResponse == null
                )
                HorizontalDivider()
                ListSelector()
                HorizontalDivider()

                EventDateTimePicker(
                    allDayEvent = allDayEvent,
                    onAllDayEventChanged = {
                        if (isEditable || isNewEvent || eventResponse == null) allDayEvent =
                            !allDayEvent
                    },
                    date = eventDate,
                    time = eventTime,
                    onDateSelected = {
                        if (isEditable || isNewEvent || eventResponse == null) eventViewModel.onEventDateChanged(
                            it
                        )
                    },
                    onTimeSelected = {
                        if (isEditable || isNewEvent || eventResponse == null) eventViewModel.onEventTimeChanged(
                            it
                        )
                    },
                    enabled = isEditable || isNewEvent || eventResponse == null
                )

                HorizontalDivider()

                DropdownMenuLocation(
                    enabled = isEditable || isNewEvent || eventResponse == null,
                    selectedLocation = eventLocation,
                    onLocationSelected = {
                        if (isEditable || isNewEvent || eventResponse == null) eventViewModel.onEventLocationChanged(
                            it
                        )
                    }
                )

                HorizontalDivider()

                DescriptionField(
                    enabled = isEditable || isNewEvent || eventResponse == null,
                    eventDescription = eventDescription,
                    onEventDescriptionChanged = {
                        if (isEditable || isNewEvent || eventResponse == null) eventViewModel.onEventDescriptionChanged(
                            it
                        )
                    }
                )

                HorizontalDivider()

                if (predictions != null && predictions != PredictionsResponse()) {
                    Log.d("EventBottomSheet", "LocalityKey: $localityKey")
                    Log.d("EventBottomSheet", "Predictions: $predictions")

                    val prediction = findPrediction(
                        predictions!!,
                        getYearFromMillis(eventDate),
                        getMonthFromMillis(eventDate),
                        getDayFromMillis(eventDate)
                    )

                    if (prediction != null) {
                        WeatherPreview(prediction = prediction)
                    }
                    AlertMessage("Las predicciones son generadas con modelos de Aprendizaje automático (IA), por lo que pueden ser no totalmente precisas.")
                }

                if (eventResponse != null && !isNewEvent) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val eventDateTime = combineDateAndTime(
                                    eventDate,
                                    if (allDayEvent) 0L else eventTime
                                )
                                val endTime = if (allDayEvent) {
                                    val endDate = Instant.ofEpochMilli(eventDate)
                                        .atZone(ZoneId.of("UTC"))
                                        .toLocalDate()
                                        .plusDays(1)
                                        .atStartOfDay(ZoneId.of("UTC"))
                                        .toInstant()
                                    endDate.toEpochMilli()
                                } else {
                                    eventDateTime + 3600000L
                                }
                                addEventToGoogleCalendar(
                                    context = context,
                                    title = eventName,
                                    description = eventDescription,
                                    location = eventLocation,
                                    startTime = eventDateTime,
                                    endTime = endTime,
                                    allDay = allDayEvent
                                )
                            }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.google_calendar),
                                    contentDescription = "Google Calendar Icon",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Agregar al calendario",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

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
                            Text("Eliminar", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }

        if (showCancelDialog) {
            AlertDialog(
                onDismissRequest = { showCancelDialog = false },
                title = { Text("Cancelar cambios") },
                text = { Text("¿Está seguro de que desea cancelar los cambios?") },
                confirmButton = {
                    TextButton(onClick = {
                        showCancelDialog = false
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
                    TextButton(onClick = { showCancelDialog = false }) {
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
                        agendaViewModel.deleteEvent(userId, eventResponse!!.id!!)
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

fun addEventToGoogleCalendar(
    context: Context,
    title: String,
    description: String,
    location: String,
    startTime: Long,
    endTime: Long,
    allDay: Boolean
) {
    Log.d("EventBottomSheet", "All day: $allDay")
    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, title)
        putExtra(CalendarContract.Events.DESCRIPTION, description)
        putExtra(CalendarContract.Events.EVENT_LOCATION, location)
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
        putExtra(CalendarContract.Events.ALL_DAY, allDay)
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "No se encontró una aplicación de calendario", Toast.LENGTH_SHORT)
            .show()
    }
}

fun findPrediction(
    predictionsResponse: PredictionsResponse,
    year: Int,
    month: Int,
    day: Int
): Prediction? {
    for (localityPrediction in predictionsResponse.predictions) {
        val yearPrediction = localityPrediction.years.find { it.year == year }
        if (yearPrediction != null) {
            val monthPrediction = yearPrediction.months.find { it.month == month }
            if (monthPrediction != null) {
                val dayPrediction = monthPrediction.days.find { it.day == day }
                if (dayPrediction != null) {
                    return dayPrediction.prediction
                }
            }
        }
    }
    return null
}
