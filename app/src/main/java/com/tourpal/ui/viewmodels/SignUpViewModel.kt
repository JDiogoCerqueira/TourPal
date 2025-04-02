package com.tourpal.ui.viewmodels

import androidx.compose.runtime.*
import com.tourpal.services.auth.AuthenticationServices
import com.tourpal.data.model.User
import androidx.lifecycle.ViewModel
import com.tourpal.services.auth.Result

// Example ViewModel
class SignupViewModel(private val authenticationServices: AuthenticationServices) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var signupResult by mutableStateOf<Result<User>?>(null)
    var isLoading by mutableStateOf(false)


    suspend fun signUp() {
        isLoading = true
        signupResult = authenticationServices.signUpWithEmail(email, password)
        isLoading = false
    }
}

