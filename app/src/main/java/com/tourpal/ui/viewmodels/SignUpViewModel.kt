package com.tourpal.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tourpal.data.model.User
import com.tourpal.data.model.repository.UserRepository
import com.tourpal.services.auth.AuthenticationServices
import com.tourpal.services.auth.Result

class SignupViewModel(
    private val authenticationServices: AuthenticationServices,
    private val userRepository: UserRepository // Inject UserRepository
) : ViewModel() {
    var realName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var signupResult by mutableStateOf<Result<User>?>(null)
    var isLoading by mutableStateOf(false)

    suspend fun signUp() {
        isLoading = true
        signupResult = authenticationServices.signUpWithEmail(email, password)
        signupResult?.let { result ->
            Log.d("SignupViewModel", "Signup result: $result")
            if (result is Result.Success) {
                val user = result.data.copy(name = realName) // Update name
                userRepository.saveUser(user) // Use UserRepository to save the updated user
            }
        }
        isLoading = false
    }
}