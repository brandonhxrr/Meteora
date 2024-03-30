package ipn.escom.meteora.ui.login

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import ipn.escom.meteora.R
import ipn.escom.meteora.data.signup.SignUpViewModel
import ipn.escom.meteora.ui.Screens
import java.io.ByteArrayOutputStream

@OptIn(
    ExperimentalGlideComposeApi::class
)
@Composable
fun SignUp(navController: NavController? = null, signUpViewModel: SignUpViewModel) {

    val username: String by signUpViewModel.username.observeAsState(initial = "")
    val email: String by signUpViewModel.email.observeAsState(initial = "")
    val password: String by signUpViewModel.password.observeAsState(initial = "")
    val repeatPassword: String by signUpViewModel.repeatPassword.observeAsState(initial = "")
    val selectedImageUri: String? by signUpViewModel.selectedImageUri.observeAsState()
    val showSignUpIndicator: Boolean by signUpViewModel.showSignUpIndicator.observeAsState(initial = false)
    val isSignUpEnabled by signUpViewModel.isSignUpEnabled.observeAsState(initial = false)
    val errorMessage: String by signUpViewModel.errorMessage.observeAsState(initial = "")
    val showError: Boolean by signUpViewModel.showError.observeAsState(initial = false)

    val registerImageActivityLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            signUpViewModel.onImageSelected(uri.toString())
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp)
                    .align(alignment = Alignment.CenterVertically)
            )
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .align(alignment = Alignment.CenterVertically),
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight(400)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.sign_up),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(alignment = Alignment.CenterHorizontally),
            color = Color.Black,
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .clickable {
                registerImageActivityLauncher.launch("image/*")
            }
            .align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                GlideImage(
                    model = selectedImageUri,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.AddAPhoto,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .background(MaterialTheme.colorScheme.onBackground)
                        .padding(20.dp),
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (showError) {
            ErrorMessage(errorMessage)
        }

        UserName(username) {
            signUpViewModel.onSignUpChanged(it, email, password, repeatPassword)
        }

        Email(email) {
            signUpViewModel.onSignUpChanged(username, it, password, repeatPassword)
        }

        Password(password = password, repeat = false, final = false) {
            signUpViewModel.onSignUpChanged(username, email, it, repeatPassword)
        }

        Password(password = repeatPassword, repeat = true, final = true) {
            signUpViewModel.onSignUpChanged(username, email, password, it)
        }

        Spacer(modifier = Modifier.height(24.dp))

        SignUpButton(
            username,
            email,
            password,
            selectedImageUri.orEmpty(),
            isSignUpEnabled,
            signUpViewModel,
            navController
        )

        GoogleSignInButton(navController)

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(id = R.string.yes_account),
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .padding(end = 8.dp),
                fontFamily = FontFamily(Font(R.font.product_sans_regular))
            )
            Text(
                text = stringResource(id = R.string.login_action),
                color = Color.Blue,
                modifier = Modifier.clickable {
                    navController?.navigate(Screens.Login.name) {
                        popUpTo(Screens.SignUp.name) {
                            inclusive = true
                        }
                    }
                },
                fontFamily = FontFamily(Font(R.font.product_sans_regular))
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
    username: String,
    email: String,
    password: String,
    selectedImageUri: String,
    enable: Boolean,
    signUpViewModel: SignUpViewModel,
    navController: NavController? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    var errorText by remember { mutableStateOf("") }

    Button(
        onClick = {
            if (enable) {
                signUpViewModel.hideError()
                keyboardController?.hide()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid

                            if (selectedImageUri != "") {
                                Glide.with(context)
                                    .asBitmap()
                                    .load(selectedImageUri)
                                    .override(500, 500)
                                    .into(object : CustomTarget<Bitmap>() {
                                        override fun onResourceReady(
                                            resource: Bitmap,
                                            transition: Transition<in Bitmap>?
                                        ) {
                                            val baos = ByteArrayOutputStream()
                                            resource.compress(
                                                Bitmap.CompressFormat.JPEG,
                                                100,
                                                baos
                                            )
                                            val imageData = baos.toByteArray()

                                            val imageRef =
                                                storageRef.child("profile_images/$userId")
                                            val uploadTask = imageRef.putBytes(imageData)

                                            uploadTask.continueWithTask { task ->
                                                if (!task.isSuccessful) {
                                                    task.exception?.let {
                                                        throw it
                                                    }
                                                }
                                                imageRef.downloadUrl
                                            }.addOnCompleteListener { downloadUrlTask ->
                                                if (downloadUrlTask.isSuccessful) {
                                                    val profileUpdates =
                                                        UserProfileChangeRequest.Builder()
                                                            .setDisplayName(username)
                                                            .setPhotoUri(downloadUrlTask.result)
                                                            .build()

                                                    auth.currentUser?.updateProfile(
                                                        profileUpdates
                                                    )

                                                    val userReference =
                                                        FirebaseDatabase.getInstance()
                                                            .getReference("users")
                                                            .child(userId.orEmpty())

                                                    val userData = hashMapOf(
                                                        "name" to username,
                                                        "email" to email,
                                                        "profileImageUrl" to downloadUrlTask.result.toString()
                                                    )

                                                    userReference.setValue(userData)
                                                        .addOnSuccessListener {
                                                            signUpViewModel.enableSignUpIndicator()
                                                            navController?.navigate(Screens.Home.name) {
                                                                popUpTo(Screens.SignUp.name) {
                                                                    inclusive = true
                                                                }
                                                            }
                                                        }
                                                        .addOnFailureListener {
                                                            signUpViewModel.disableSignUpIndicator()
                                                        }
                                                } else {
                                                    // Manejar error al obtener la URL de la imagen
                                                }
                                            }
                                        }

                                        override fun onLoadCleared(placeholder: Drawable?) {
                                        }
                                    })

                            } else {
                                val userReference = FirebaseDatabase.getInstance()
                                    .getReference("users").child(userId.orEmpty())

                                val userData = hashMapOf(
                                    "name" to username,
                                    "email" to email,
                                    "profileImageUrl" to ""
                                )

                                userReference.setValue(userData)
                                    .addOnSuccessListener {
                                        signUpViewModel.disableSignUpIndicator()
                                        navController?.navigate(Screens.Home.name) {
                                            popUpTo(Screens.SignUp.name) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                    .addOnFailureListener {
                                        signUpViewModel.disableSignUpIndicator()
                                    }
                            }
                        } else {
                            errorText = try {
                                throw task.exception!!
                            } catch (e: FirebaseAuthUserCollisionException) {
                                signUpViewModel.disableSignUpIndicator()
                                "Correo ya registrado"
                            } catch (e: Exception) {
                                signUpViewModel.disableSignUpIndicator()
                                "Error en el registro"
                            }

                            Toast.makeText(
                                context,
                                errorText,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
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
            .height(50.dp)
    ) {
        Text(text = stringResource(id = R.string.sign_up))
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    SignUp(signUpViewModel = SignUpViewModel())
}