package ipn.escom.meteora.ui

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun DisconnectedScreen(onRetry :() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No hay conexión a Internet",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(onClick = { onRetry() }) {
            Text("Reintentar")
        }
    }
}

suspend fun isInternetAvailable(): Boolean = withContext(Dispatchers.IO) {
    return@withContext try {
        val url = URL("https://www.google.com")
        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.connectTimeout = 3000
        urlConnection.connect()
        urlConnection.responseCode == 200
    } catch (e: Exception) {
        Log.e("Internet", "Internet error: $e")
        false
    }
}

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
suspend fun isNetworkAvailable(context: Context): Boolean = withContext(Dispatchers.IO) {
    try {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return@withContext false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return@withContext false
        return@withContext when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } catch (e: Exception) {
        Log.e("Network", "Network error: $e")
        return@withContext false
    }
}

fun isNetworkAvailable2(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}


fun hasInternetAccess(): Boolean {
    return try {
        val url = URL("https://www.google.com")
        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.connectTimeout = 3000 // 3 segundos
        urlConnection.connect()
        true
    } catch (e: IOException) {
        false
    }
}

