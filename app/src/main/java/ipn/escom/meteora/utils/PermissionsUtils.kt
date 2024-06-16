package ipn.escom.meteora.utils

import android.Manifest
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import ipn.escom.meteora.R

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
            title = { Text(stringResource(R.string.location_permission)) },
            text = { Text(stringResource(R.string.location_permission_details)) },
            confirmButton = {
                Button(
                    onClick = {
                        permissionState.launchPermissionRequest()
                    }
                ) {
                    Text(stringResource(id = R.string.ok))
                }
            }
        )
    }

    isLocationPermissionGranted.value = permissionState.status.isGranted
}