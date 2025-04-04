package com.tourpal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.tourpal.data.model.repository.UserRepository
import com.tourpal.services.auth.AuthenticationServices

class SignupViewModelFactory(
    private val authServices: AuthenticationServices,
    private val userRepository: UserRepository // Inject UserRepository

) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            return SignupViewModel(authServices, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}