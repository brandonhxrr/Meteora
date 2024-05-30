package ipn.escom.meteora.data.authentication.data.network.response

import com.google.firebase.auth.FirebaseUser

sealed class AuthenticationResponse {
    data class Success(val user: FirebaseUser): AuthenticationResponse()
    data class Error(val message: String): AuthenticationResponse()
}
