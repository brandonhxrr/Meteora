package ipn.escom.meteora.data.authentication.data

import android.content.Context
import ipn.escom.meteora.data.authentication.data.network.AuthenticationService
import ipn.escom.meteora.data.authentication.data.network.response.AuthenticationResponse

class AuthenticationRepository() {

    private val authenticationService: AuthenticationService = AuthenticationService()

    suspend fun signUp(
        context: Context,
        username: String,
        email: String,
        password: String,
        selectedImageUri: String?
    ): AuthenticationResponse {
        return authenticationService.signUp(context, username, email, password, selectedImageUri)
    }

    suspend fun signIn(context: Context, email: String, password: String): AuthenticationResponse {
        return authenticationService.signIn(context, email, password)
    }
}
