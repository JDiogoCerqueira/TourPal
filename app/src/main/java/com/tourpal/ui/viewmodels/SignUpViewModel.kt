package com.tourpal.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tourpal.data.model.User
import com.tourpal.services.auth.AuthenticationServices
import com.tourpal.services.auth.Result
import com.tourpal.services.firestore.FirestoreService

class SignupViewModel(
    private val authenticationServices: AuthenticationServices,
    private val firestoreService: FirestoreService = FirestoreService()
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
                firestoreService.saveUser(user) // Save updated user with real name
            }
        }
        isLoading = false
    }
}