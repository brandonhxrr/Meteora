package ipn.escom.meteora

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ipn.escom.meteora.ui.SplashScreen

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen()
        }
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}