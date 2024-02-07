package ipn.escom.meteora.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.firebase.auth.FirebaseAuth
import ipn.escom.meteora.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun Home(navController: NavController?) {
    var selectedItem by remember { mutableIntStateOf(0) }
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
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "App logo",
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = stringResource(id = R.string.app_name),
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.align(Alignment.CenterVertically),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController?.navigate(Screens.User.name)
                    }) {
                        if (auth.currentUser?.photoUrl != null) {
                            GlideImage(
                                model = auth.currentUser?.photoUrl,
                                contentDescription = "User profile picture",
                                modifier = Modifier.clip(
                                    CircleShape
                                )
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = "User profile picture"
                            )
                        }

                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
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
                        label = { Text(stringResource(id = item)) })
                }
            }
        }
    ) {
        Forecast(modifier = Modifier.padding(it))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    Home(null)
}