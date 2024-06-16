package ipn.escom.meteora.data.authentication.data.network

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import ipn.escom.meteora.R
import ipn.escom.meteora.data.authentication.data.network.response.AuthenticationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class AuthenticationService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    suspend fun signUp(
        context: Context,
        username: String,
        email: String,
        password: String,
        selectedImageUri: String?
    ): AuthenticationResponse {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val user = auth.currentUser ?: throw Exception("User not found")
            uploadProfilePicture(context, user, username, selectedImageUri)
            AuthenticationResponse.Success(user)
        } catch (e: FirebaseAuthUserCollisionException) {
            AuthenticationResponse.Error(context.getString(R.string.email_already_used))
        } catch (e: Exception) {
            AuthenticationResponse.Error(context.getString(R.string.error_in_registration))
        }
    }

    suspend fun signIn(context: Context, email: String, password: String): AuthenticationResponse {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            val user = auth.currentUser ?: throw Exception("User not found")
            AuthenticationResponse.Success(user)
        } catch (e: FirebaseAuthInvalidUserException) {
            AuthenticationResponse.Error(context.getString(R.string.user_not_found))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthenticationResponse.Error("Credenciales inválidas")
        } catch (e: Exception) {
            AuthenticationResponse.Error("Error en la autenticación")
        }
    }

    private suspend fun uploadProfilePicture(
        context: Context,
        user: FirebaseUser,
        username: String,
        selectedImageUri: String?
    ) {
        if (!selectedImageUri.isNullOrEmpty()) {
            val baos = ByteArrayOutputStream()
            val bitmap = withContext(Dispatchers.IO) {
                Glide.with(context).asBitmap().load(selectedImageUri).submit().get()
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageData = baos.toByteArray()
            val imageRef = storageRef.child("profile_images/${user.uid}")
            imageRef.putBytes(imageData).await()
            val downloadUrl = imageRef.downloadUrl.await()
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .setPhotoUri(downloadUrl)
                .build()
            user.updateProfile(profileUpdates).await()
        } else {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build()
            user.updateProfile(profileUpdates).await()
        }
    }
}
