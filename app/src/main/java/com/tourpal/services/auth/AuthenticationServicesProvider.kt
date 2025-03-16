package com.tourpal.services.auth

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth

object AuthenticationServiceProvider {
    fun provideAuthenticationService(context: Context): AuthenticationServices {
        val firebaseAuth = FirebaseAuth.getInstance()
        val credentialManager = CredentialManager.create(context)
        return AuthenticationServicesImpl(firebaseAuth, credentialManager)
    }
}