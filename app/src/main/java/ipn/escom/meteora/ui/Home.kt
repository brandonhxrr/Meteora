package ipn.escom.meteora.ui

import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import ipn.escom.meteora.R
import ipn.escom.meteora.data.events.AgendaViewModel
import ipn.escom.meteora.data.localities.LocalityViewModel
import ipn.escom.meteora.data.localities.SearchBarWithDialog
import ipn.escom.meteora.data.predictions.PredictionsViewModel
import ipn.escom.meteora.data.weather.WeatherViewModel
import ipn.escom.meteora.ui.agenda.AgendaScreen
import ipn.escom.meteora.ui.theme.getBackground
import ipn.escom.meteora.ui.theme.getOnBackground
import ipn.escom.meteora.ui.theme.lightBackground
import ipn.escom.meteora.utils.RequestLocationPermission
import ipn.escom.meteora.utils.getLocation
import ipn.escom.meteora.utils.getPostalCode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun Home(navController: NavController?, weatherViewModel: WeatherViewModel?) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var location by remember { mutableStateOf<Location?>(null) }
    var postalCode by remember { mutableStateOf<String?>(null) }
    val isLocationPermissionGranted = remember { mutableStateOf(false) }
    var selectedItem by remember { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    var hasInternetAccess: Boolean by remember {
        mutableStateOf(isNetworkAvailable2(context))
    }
    val state = rememberPullToRefreshState()

    val localitiesViewModel = LocalityViewModel()
    val agendaViewModel = AgendaViewModel()

    LaunchedEffect(true) {
        hasInternetAccess = isInternetAvailable()
    }

    RequestLocationPermission(isLocationPermissionGranted)

    LaunchedEffect(isLocationPermissionGranted.value, true) {
        location = getLocation(fusedLocationClient, isLocationPermissionGranted, context)
        postalCode = getPostalCode(context, location)
    }

    if (state.isRefreshing) {
        LaunchedEffect(true) {
            hasInternetAccess = isInternetAvailable()
            delay(1500)
            state.endRefresh()
        }
    }

    val auth = FirebaseAuth.getInstance()

    val items =
        listOf(R.string.menu_home, R.string.menu_map, R.string.menu_agenda, R.string.menu_forecast)
    val icons = listOf(
        Icons.Rounded.Home,
        Icons.Rounded.Map,
        Icons.Rounded.CalendarMonth,
        Icons.Rounded.WbSunny
    )

    Scaffold(
        containerColor = getBackground(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = getBackground()
                ),
                title = {
                SearchBarWithDialog(
                    postalCode = postalCode ?: "",
                    localityViewModel = localitiesViewModel,
                    onLocationSelected = {
                        location = it

                    },
                    onLocationClear = {
                        coroutineScope.launch {
                            location = getLocation(
                                fusedLocationClient,
                                isLocationPermissionGranted,
                                context
                            )
                        }
                    })
                Spacer(modifier = Modifier.width(8.dp))
            },
                actions = {
                    IconButton(
                        onClick = {
                            navController?.navigate(Screens.User.name)
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                    ) {
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
                    }
                })
        },
        bottomBar = {
            NavigationBar(
                containerColor = getOnBackground(),
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        icon = {
                            Icon(
                                imageVector = icons[index],
                                contentDescription = stringResource(id = item)
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(id = item),
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            selectedIconColor = getOnBackground()
                        ))
                }
            }
        }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (true) {
                when (selectedItem) {
                    0 -> {
                        Forecast(
                            modifier = Modifier.padding(it),
                            weatherViewModel!!,
                            location,
                            navController
                        )
                    }

                    1 -> {
                        MapsScreen(
                            modifier = Modifier.padding(it),
                            location = location,
                            apiKey = stringResource(
                                id = R.string.OpenWeatherAPIKEY
                            )
                        )
                    }

                    2 -> {
                        AgendaScreen(
                            modifier = Modifier.padding(it),
                            agendaViewModel = agendaViewModel
                        )
                    }

                    3 -> {
                        PredictionsScreen(
                            modifier = Modifier.padding(it),
                            location = location,
                            predictionsViewModel = PredictionsViewModel(),
                            agendaViewModel = agendaViewModel
                        )
                    }
                }
            } else {
                DisconnectedScreen {
                    state.startRefresh()
                }
            }
            PullToRefreshContainer(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(it),
                state = state,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    Home(null, null)
}