package ipn.escom.meteora.ui.agenda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ipn.escom.meteora.data.events.Event
import ipn.escom.meteora.data.events.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEventScreen(navController: NavController, eventViewModel: EventViewModel) {

    val eventName: String by eventViewModel.eventName.observeAsState("")
    val eventDescription by eventViewModel.eventDescription.observeAsState("")
    val eventTime by eventViewModel.eventTime.observeAsState("")
    val eventDate by eventViewModel.eventDate.observeAsState("")
    val eventLocation by eventViewModel.eventLocation.observeAsState("")
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Agregar evento", style = MaterialTheme.typography.headlineMedium) },
            navigationIcon = {
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Atrás")
                }
            })
    }, content = { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(value = eventName,
                onValueChange = { eventViewModel.onEventNameChanged(it) },
                label = { Text("Nombre del Evento") })

            OutlinedTextField(value = eventDescription,
                onValueChange = { eventViewModel.onEventDescriptionChanged(it) },
                label = { Text("Descripción del Evento") })

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
                    navController.popBackStack()
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
                        navController.navigateUp()
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
    })
}


