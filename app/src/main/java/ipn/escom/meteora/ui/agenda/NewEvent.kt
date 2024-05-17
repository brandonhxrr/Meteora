package ipn.escom.meteora.ui.agenda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.firebase.auth.FirebaseAuth
import ipn.escom.meteora.data.events.Event
import ipn.escom.meteora.data.events.EventViewModel
import ipn.escom.meteora.ui.theme.amber
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
        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = eventName,
                onValueChange = { eventViewModel.onEventNameChanged(it) },
                placeholder = {
                    Text(
                        "Agregar título", style = MaterialTheme.typography.titleLarge
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
                textStyle = MaterialTheme.typography.titleLarge,
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

            OutlinedTextField(
                value = eventDescription,
                onValueChange = { eventViewModel.onEventDescriptionChanged(it) },
                label = { Text("Descripción del Evento") },
                modifier = Modifier.fillMaxWidth()
            )

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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ListSelector() {
    val auth = FirebaseAuth.getInstance()
    //val auth = null
    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        //if(auth != null) {
        if (auth.currentUser?.photoUrl != null) {
            GlideImage(
                model = auth.currentUser?.photoUrl,
                contentDescription = "User profile picture",
                modifier = Modifier
                    .clip(
                        CircleShape
                    )
                    .size(40.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "User profile picture"
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Row {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(10.dp)
                        .background(amber)
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Eventos",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = auth.currentUser?.email ?: "" ,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun EventDateTimePicker(
    allDayEvent: Boolean,
    onAllDayEventChanged: (Boolean) -> Unit,
date: Long,
time: Long,
onDateSelected: (Long) -> Unit,
onTimeSelected: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.AccessTime, contentDescription = null,
            modifier = Modifier.padding(vertical = 12.dp).size(24.dp)
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
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                DatePickerField(date = date, onDateSelected = { onDateSelected(it) })
                if (!allDayEvent) {
                    TimePickerField(
                        time = time,
                        onTimeSelected = { onTimeSelected(it) }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EventDateTimePickerPreview() {
    EventDateTimePicker(
        allDayEvent = false,
        onAllDayEventChanged = {},
        date = 0L,
        time = 0L,
        onDateSelected = {},
        onTimeSelected = {}
    )
}

