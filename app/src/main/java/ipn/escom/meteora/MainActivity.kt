package ipn.escom.meteora

import android.Manifest
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import ipn.escom.meteora.ui.Home
import ipn.escom.meteora.ui.Login
import ipn.escom.meteora.ui.Screens
import ipn.escom.meteora.ui.SignUp
import ipn.escom.meteora.ui.Splash
import ipn.escom.meteora.ui.UserScreen
import ipn.escom.meteora.ui.theme.MeteoraTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        setContent {
            MeteoraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Start()
                }
            }
        }
    }

}

@Composable
fun Start() {
    val navController = rememberNavController()
    val user = FirebaseAuth.getInstance().currentUser

    val requestStoragePermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {

    }

    NavHost(
        navController = navController,
        startDestination = Screens.Splash.name
    ) {
        composable(Screens.Splash.name) {
            Splash()
            val timer = object : CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    if (navController.currentBackStackEntry?.destination?.route == Screens.Splash.name) {
                        if (user != null) {
                            navController.navigate(Screens.Home.name) {
                                popUpTo(Screens.Splash.name) {
                                    inclusive = true
                                }
                            }
                        } else {
                            requestStoragePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            navController.navigate(Screens.Login.name) {
                                popUpTo(Screens.Splash.name) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            }

            timer.start()

        }
        composable(Screens.Home.name) {
            Home(navController = navController)
        }

        composable(Screens.Login.name) {
            Login(navController = navController)
        }

        composable(Screens.SignUp.name) {
            SignUp(navController = navController)
        }

        composable(Screens.User.name) {
            UserScreen(navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MeteoraTheme {
        Start()
    }
}
