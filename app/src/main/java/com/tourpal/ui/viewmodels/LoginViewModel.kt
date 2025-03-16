package com.tourpal.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tourpal.services.auth.AuthenticationServices
import com.tourpal.services.auth.AuthenticationServiceProvider
import com.tourpal.services.auth.Result
import com.tourpal.services.auth.User
import kotlinx.coroutines.launch

data class LoginState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: User? = null
)

class LoginViewModel(
    private val authService: AuthenticationServices
) : ViewModel() {

    var state = mutableStateOf(LoginState())
        private set

    fun signInWithGoogle(context: android.content.Context) {
        viewModelScope.launch {
            try {
                state.value = state.value.copy(isLoading = true, error = null)
                when (val result = authService.signInWithGoogle(context)) {
                    is Result.Success -> {
                        state.value = state.value.copy(isLoading = false, user = result.data)
                    }
                    is Result.Failure -> {
                        val errorMessage = when (result.exception) {
                            //Check the exception type, or the message contents to provide a custom error message
                            //is NetworkErrorException -> "Please check your network connection and try again."
                            else -> "Google Sign-In failed. Please try again." // Default message
                        }

                        // Log the exception for debugging
                        Log.e("LoginViewModel", "Google Sign-In error", result.exception)

                        state.value = state.value.copy(
                            isLoading = false,
                            error = errorMessage
                        )
                    }
                }
            } catch (e: Exception) {
                // Handle unexpected exceptions
                Log.e("LoginViewModel", "Unexpected error during Google Sign-In", e)
                state.value = state.value.copy(
                    isLoading = false,
                    error = "An unexpected error occurred. Please try again."
                )
            } finally {
                //Ensure is loading is false.
                state.value = state.value.copy(isLoading = false)
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            state.value = state.value.copy(isLoading = true, error = null)
            when (val result = authService.signInWithEmail(email, password)) {
                is Result.Success -> {
                    state.value = state.value.copy(isLoading = false, user = result.data)
                }
                is Result.Failure -> {
                    state.value = state.value.copy(
                        isLoading = false,
                        error = result.exception.message ?: "Email/Password Sign-In failed"
                    )
                }
            }
        }
    }

    companion object {
        fun create(context: android.content.Context): LoginViewModel {
            val authService = AuthenticationServiceProvider.provideAuthenticationService(context)
            return LoginViewModel(authService)
        }
    }
}