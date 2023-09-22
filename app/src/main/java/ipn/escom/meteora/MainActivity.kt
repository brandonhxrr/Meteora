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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ipn.escom.meteora.ui.HomeScreen
import ipn.escom.meteora.ui.Screens
import ipn.escom.meteora.ui.SplashScreen
import ipn.escom.meteora.ui.theme.MeteoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    NavHost(
        navController = navController,
        startDestination = Screens.Splash.name
    ){
        composable(Screens.Splash.name){
            SplashScreen()

            val timer = object : CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    navController.navigate(Screens.Home.name)
                }
            }

            timer.start()
        }

        composable(Screens.Home.name){
            HomeScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    MeteoraTheme {
        SplashScreen()
    }
}