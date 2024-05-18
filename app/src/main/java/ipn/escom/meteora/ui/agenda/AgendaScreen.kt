package ipn.escom.meteora.ui.agenda

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ipn.escom.meteora.data.events.EventViewModel
import ipn.escom.meteora.data.events.data.network.response.EventResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(modifier: Modifier = Modifier, navController: NavController? = null) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val eventViewModel = EventViewModel()
    val userId = eventViewModel.userId.value
    val userEvents by eventViewModel.userEvents.observeAsState()

    val eventDetailSheetState = rememberModalBottomSheetState()
    var showDetailsheet by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<EventResponse?>(null) }

    LaunchedEffect(key1 = true) {
        eventViewModel.getEvents(userId!!)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showBottomSheet = true
            }) {
                Icon(Icons.Rounded.Add, contentDescription = "Add event")
            }
        },
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Próximos eventos",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterVertically)
                    )
                    SortButton(modifier = Modifier.align(Alignment.CenterVertically)) { ascending ->
                        eventViewModel.onEventOrderChanged(ascending)
                    }
                }
            }

            item {
                userEvents?.forEach { event ->
                    Log.d("AgendaScreen", "${event.id}")
                    EventItem(eventResponse = event, onClick = {
                        selectedEvent = event
                        showDetailsheet = true
                    })
                }
            }
            item {
                if (showBottomSheet) {
                    NewEventBottomSheet(
                        eventViewModel = EventViewModel(),
                        sheetState = modalBottomSheetState,
                        scope = coroutineScope,
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                        onEventAdded = {
                            eventViewModel.getEvents(userId!!)
                        }
                    )
                }
            }

            item {
                if (showDetailsheet && selectedEvent != null) {
                    EventDetailBottomSheet(
                        eventResponse = selectedEvent!!,
                        sheetState = eventDetailSheetState,
                        scope = coroutineScope,
                        onDismissRequest = {
                            showDetailsheet = false
                        },
                        onEventUpdated = {
                            eventViewModel.getEvents(userId!!)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AgendaScreenPreview() {
    AgendaScreen()
}