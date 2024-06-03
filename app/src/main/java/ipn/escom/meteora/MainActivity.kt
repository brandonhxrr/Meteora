package ipn.escom.meteora

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import ipn.escom.meteora.data.PreferencesViewModel
import ipn.escom.meteora.data.authentication.login.LoginViewModel
import ipn.escom.meteora.data.authentication.signup.SignUpViewModel
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch(Dispatchers.Default) {
            FirebaseApp.initializeApp(applicationContext)
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
            firebaseAnalytics = FirebaseAnalytics.getInstance(this@MainActivity)
        }

        setContent {
            MeteoraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Start(firebaseAnalytics = firebaseAnalytics)
                }
            }
        }
    }
}

@Composable
fun Start(firebaseAnalytics: FirebaseAnalytics?) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser
    val preferencesViewModel = PreferencesViewModel(context)
    val weatherViewModel = WeatherViewModel(context, preferencesViewModel)
    val signUpViewModel = SignUpViewModel()

    navController.addOnDestinationChangedListener { _, destination, _ ->
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.SCREEN_NAME, destination.label as String?)
        params.putString(FirebaseAnalytics.Param.SCREEN_CLASS, destination.label as String?)
        firebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, params)
    }

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

    NavHost(
        navController = navController,
        startDestination = Screens.Splash.name
    ) {

        composable(Screens.Splash.name) {
            SplashScreen()
        }

        composable(Screens.Home.name) {
            Home(
                navController = navController,
                weatherViewModel = weatherViewModel,
                preferencesViewModel = preferencesViewModel
            )
        }

        composable("dailyForecast/{town}/{timestamp}") { backStackEntry ->
            val town = backStackEntry.arguments?.getString("town")
            val timestamp = backStackEntry.arguments?.getString("timestamp")

            WeatherDetailScreen(
                preferencesViewModel = preferencesViewModel,
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
            UserScreen(navController = navController, preferencesViewModel = preferencesViewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MeteoraTheme {
        Start(null)
    }
}
