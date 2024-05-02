package ipn.escom.meteora

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import ipn.escom.meteora.data.login.LoginViewModel
import ipn.escom.meteora.data.signup.SignUpViewModel
import ipn.escom.meteora.data.weather.WeatherViewModel
import ipn.escom.meteora.ui.Home
import ipn.escom.meteora.ui.Screens
import ipn.escom.meteora.ui.SplashScreen
import ipn.escom.meteora.ui.UserScreen
import ipn.escom.meteora.ui.WeatherDetailScreen
import ipn.escom.meteora.ui.login.Login
import ipn.escom.meteora.ui.login.SignUp1
import ipn.escom.meteora.ui.login.SignUp2
import ipn.escom.meteora.ui.theme.MeteoraTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(applicationContext)
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

    NavHost(
        navController = navController,
        startDestination = Screens.Splash.name
    ) {
        val signUpViewModel = SignUpViewModel()

        composable(Screens.Splash.name) {
            SplashScreen()
            val timer = object : CountDownTimer(1500, 1000) {
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
        val weatherViewModel = WeatherViewModel()

        composable(Screens.Home.name) {
            Home(navController = navController, weatherViewModel = weatherViewModel)
        }

        composable("dailyForecast/{town}/{timestamp}") { backStackEntry ->
            val town = backStackEntry.arguments?.getString("town")
            Log.d("town", town ?: "null")
            val timestamp = backStackEntry.arguments?.getString("timestamp")
            Log.d("timestamp", timestamp ?: "null")

            WeatherDetailScreen(
                town = town,
                timestamp = timestamp,
                weatherViewModel = weatherViewModel,
                navController = navController
            )
        }

        composable(Screens.Login.name) {
            Login(navController = navController, LoginViewModel())
        }

        composable(Screens.SignUp1.name) {
            SignUp1(navController = navController, signUpViewModel)
        }

        composable(Screens.SignUp2.name) {
            SignUp2(navController = navController, signUpViewModel)
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
