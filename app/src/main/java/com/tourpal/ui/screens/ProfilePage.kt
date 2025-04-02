package com.tourpal.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tourpal.services.auth.AuthenticationServices
import com.tourpal.ui.components.DefaultButton
import kotlinx.coroutines.launch
import com.tourpal.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.LaunchedEffect
import com.google.firebase.auth.FirebaseAuth
import coil.compose.AsyncImage


@Composable
fun ProfileScreen(authServices: AuthenticationServices, onSignOutSuccess: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var signOutStatus by remember { mutableStateOf<String?>(null) }
    val currentUser = FirebaseAuth.getInstance().currentUser // Get the current Firebase user

    var userData by remember { mutableStateOf<User?>(null) }  // Hold user data from Firestore


    // Fetch user data from Firestore
    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let {
            FirebaseFirestore.getInstance().collection("user")  // Use 'user' collection
                .document(it)  // Fetch user document by UID
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val user = documentSnapshot.toObject(User::class.java)
                        userData = user
                    }
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Profile Screen")

        // Display user data if available
        userData?.let { user ->
            Text(text = "Welcome, ${user.name}")
            Text(text = "Email: ${user.email}")
            Text(text = "Name: ${user.name}")

            // Display the user's profile photo (if available)
            if (user.profilePhoto.isNotEmpty()) {
                AsyncImage(
                    model = user.profilePhoto,  // Profile photo URL
                    contentDescription = "Profile Photo",
                    modifier = Modifier.size(100.dp)  // Correctly applying the size modifier
                )
            }
        }

        // Sign Out Button
        DefaultButton(
            onClick = {
                coroutineScope.launch {
                    signOutStatus = "Signing out..."
                    when (val result = authServices.signOut()) {
                        is com.tourpal.services.auth.Result.Failure -> {
                            signOutStatus = "Sign out failed: ${result.exception.message}"
                        }
                        is com.tourpal.services.auth.Result.Success<*> -> {
                            signOutStatus = "Signed out successfully"
                            onSignOutSuccess() // Navigate to login screen or handle success
                        }
                    }
                }
            },
            enabled = currentUser != null, // Disable button if no user is signed in
            s = "Sign Out"
        )

        // Display sign out status
        signOutStatus?.let {
            Text(
                text = it,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

