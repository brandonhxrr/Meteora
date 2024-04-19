package ipn.escom.meteora.utils

import android.Manifest
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(isLocationPermissionGranted: MutableState<Boolean>) {
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    LaunchedEffect(permissionState) {
        permissionState.launchPermissionRequest()
    }

    if (permissionState.status.shouldShowRationale) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Permiso de ubicación") },
            text = { Text("Se necesita el permiso de ubicación para mostrar el clima en tu ubicación actual") },
            confirmButton = {
                Button(
                    onClick = {
                        permissionState.launchPermissionRequest()
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }

    isLocationPermissionGranted.value = permissionState.status.isGranted
}