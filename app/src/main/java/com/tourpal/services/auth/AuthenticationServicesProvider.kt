package com.tourpal.services.auth

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.tourpal.data.model.repository.UserRepository
import com.tourpal.services.firestore.FirestoreService

object AuthenticationServiceProvider {
    fun provideAuthenticationService(context: Context): AuthenticationServices {
        val firebaseAuth = FirebaseAuth.getInstance()
        val credentialManager = CredentialManager.create(context)
        val userRepository = UserRepository(FirestoreService())
        return AuthenticationServicesImpl(firebaseAuth, credentialManager, userRepository = userRepository)
    }
}