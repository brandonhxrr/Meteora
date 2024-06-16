package ipn.escom.meteora.data.authentication.domain

import android.content.Context
import ipn.escom.meteora.data.authentication.data.AuthenticationRepository
import ipn.escom.meteora.data.authentication.data.network.response.AuthenticationResponse


class AuthenticationUseCase() {

    private val authenticationRepository = AuthenticationRepository()

    suspend fun signUp(
        context: Context,
        username: String,
        email: String,
        password: String,
        selectedImageUri: String?
    ): AuthenticationResponse {
        return authenticationRepository.signUp(context, username, email, password, selectedImageUri)
    }

    suspend fun signIn(context: Context, email: String, password: String): AuthenticationResponse {
        return authenticationRepository.signIn(context, email, password)
    }
}
