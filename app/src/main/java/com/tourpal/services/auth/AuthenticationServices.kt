package com.tourpal.services.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.tourpal.R
import kotlinx.coroutines.tasks.await

// TODO: Properly define the User model
data class User(
    val id: String,
    val email: String,
    val name: String,
)


sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val exception: Exception) : Result<Nothing>()
}

interface AuthenticationServices {
    suspend fun signInWithGoogle(context: Context): Result<User>
    suspend fun signInWithEmail(email: String, password: String): Result<User>
    suspend fun signUpWithEmail(email: String, password: String): Result<User>
    suspend fun signOut(): Result<Unit>
    fun getCurrentUser(): User?
}

class AuthenticationServicesImpl(
    private val firebaseAuth: FirebaseAuth,
    private val credentialManager: CredentialManager,
) : AuthenticationServices {


    // TODO: Additional flow for when a user is already registered with email and password and wants to sign in with Google and vice-versa

    override suspend fun signInWithGoogle(context: Context): Result<User> {
        return try {
            // Set up Google Sign-In option using Credential Manager
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false) // Present all Google accounts to the user
                .setServerClientId(context.getString(R.string.web_client_id))
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            // Use Credential Manager to get the Google credential
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )
            val credential = result.credential

            // Extract the ID token from the Google credential
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)
                val idTokenString = googleIdTokenCredential.idToken

                // Authenticate with Firebase using the Google ID token
                val firebaseCredential = GoogleAuthProvider.getCredential(idTokenString, null)
                val authResult: AuthResult = firebaseAuth.signInWithCredential(firebaseCredential).await()
                val firebaseUser = authResult.user
                    ?: return Result.Failure(Exception("Firebase user not found"))

                // Create and return User
                val user = User(
                    id = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    name = firebaseUser.displayName ?: "Unknown"
                )
                Result.Success(user)
            } else {
                // Catch any unrecognized custom credential type here.
                Log.e("AuthenticationServices", "Unexpected type of credential")
                Result.Failure(Exception("Unexpected credential type"))
            }
        } catch (e: NoCredentialException) {
            // No matching credentials found, meaning the user has no Google account or
            // hasn't authorized this app to use their Google account
            Result.Failure(e)
        } catch (e: GetCredentialException) {
            Result.Failure(e)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun signInWithEmail(email: String, password: String): Result<User> {
        return try {
            // Validate input
            if (email.isBlank() || password.isBlank()) {
                return Result.Failure(Exception("Email and password cannot be empty"))
            }

            // Sign in with Firebase
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
                ?: return Result.Failure(Exception("Firebase user not found"))

            // Create a User object and save it
            val user = User(
                id = firebaseUser.uid,
                email = firebaseUser.email ?: "",
                name = firebaseUser.displayName ?: "Unknown"
            )

            Result.Success(user)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun signUpWithEmail(email: String, password: String): Result<User> {
        return try {
            // Validate input
            if (email.isBlank() || password.isBlank()) {
                return Result.Failure(Exception("Email and password cannot be empty"))
            }

            // Sign up with Firebase
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
                ?: return Result.Failure(Exception("Firebase user not found"))

            // Create a User object and save it
            val user = User(
                id = firebaseUser.uid,
                email = firebaseUser.email ?: "",
                name = firebaseUser.displayName ?: "Unknown"
            )

            Result.Success(user)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser ?: return null
        return User(
            id = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            name = firebaseUser.displayName ?: "Unknown"
        )
    }

}