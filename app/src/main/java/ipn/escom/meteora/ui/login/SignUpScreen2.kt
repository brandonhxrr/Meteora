package ipn.escom.meteora.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ipn.escom.meteora.R
import ipn.escom.meteora.data.authentication.signup.SignUpViewModel
import ipn.escom.meteora.ui.Screens

@Composable
fun SignUp2(navController: NavController? = null, signUpViewModel: SignUpViewModel) {

    val email: String by signUpViewModel.email.observeAsState(initial = "")
    val password: String by signUpViewModel.password.observeAsState(initial = "")
    val repeatPassword: String by signUpViewModel.repeatPassword.observeAsState(initial = "")
    val showSignUpIndicator: Boolean by signUpViewModel.showSignUpIndicator.observeAsState(initial = false)
    val isSignUpEnabled by signUpViewModel.isSignUpEnabled.observeAsState(initial = false)
    val errorMessage: String by signUpViewModel.errorMessage.observeAsState(initial = "")
    val showError: Boolean by signUpViewModel.showError.observeAsState(initial = false)
    val signUpSuccess: Boolean by signUpViewModel.signUpSuccess.observeAsState(initial = false)

    LaunchedEffect(key1 = signUpSuccess, block = {
        if (signUpSuccess) {
            navController?.navigate(Screens.Home.name) {
                popUpTo(Screens.SignUp1.name) {
                    inclusive = true
                }
            }
        }
    })


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Header(title = stringResource(id = R.string.sign_up), subtitle = "Queremos conocerte mejor")

        Spacer(modifier = Modifier.height(24.dp))

        if (showError) {
            ErrorMessage(errorMessage)
        }

        Email(email) {
            signUpViewModel.onSignUpChanged(it, password, repeatPassword)
        }

        Password(password = password, repeat = false, final = false) {
            signUpViewModel.onSignUpChanged(email, it, repeatPassword)
        }

        Password(password = repeatPassword, repeat = true, final = true) {
            signUpViewModel.onSignUpChanged(email, password, it)
        }

        Spacer(modifier = Modifier.height(24.dp))

        SignUpButton(
            isSignUpEnabled,
            signUpViewModel
        )

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(id = R.string.yes_account),
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .padding(end = 8.dp),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = stringResource(id = R.string.login_action),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController?.navigate(Screens.Login.name) {
                        popUpTo(Screens.SignUp1.name) {
                            inclusive = true
                        }
                    }
                },
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (showSignUpIndicator) {
            AlertDialog(
                onDismissRequest = {
                },
                title = {
                    Text(text = "Registrando usuario")
                },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.PersonAdd,
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text("No salgas de la aplicación")
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {

                }
            )
        }
    }
}

@Composable
fun SignUpButton(
    enable: Boolean,
    signUpViewModel: SignUpViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    Button(
        shape = MaterialTheme.shapes.small,
        onClick = {
            if (enable) {
                keyboardController?.hide()
                signUpViewModel.signUp(context)
            } else {
                signUpViewModel.showError()
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(50.dp)
    ) {
        Text(
            text = stringResource(id = R.string.sign_up),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    SignUp2(signUpViewModel = SignUpViewModel())
}