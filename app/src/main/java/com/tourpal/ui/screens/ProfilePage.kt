package com.tourpal.ui.screens

import TopBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.tourpal.R
import com.tourpal.data.model.User
import com.tourpal.services.auth.AuthenticationServices
import com.tourpal.ui.components.DefaultButton
import com.tourpal.ui.viewmodels.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navController: NavHostController,
    authServices: AuthenticationServices,
    onSignOutSuccess: () -> Unit,
    userViewModel: UserViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    var signOutStatus by remember { mutableStateOf<String?>(null) }
    val currentUser = FirebaseAuth.getInstance().currentUser

    // Observe user data from the ViewModel
    val userData by userViewModel.user.collectAsState()
    val error by userViewModel.error.collectAsState()

    // Load user data when available
    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { userViewModel.loadUser(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar("My Profile")

        Spacer(modifier = Modifier.height(16.dp))

        error?.let {
            Text(
                text = "Error: $it",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        userData?.let { user: User ->
            // Profile photo section styled like UpdateProfilePage
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    if (user.profilePhoto.isNotEmpty()) {
                        AsyncImage(
                            model = user.profilePhoto,
                            contentDescription = "Profile Photo",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.profile_photo_placeholder),
                            contentDescription = "Profile Photo Placeholder",
                            modifier = Modifier.size(60.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display user details in a clean vertical layout
            Text(
                text = "Welcome, ${user.name}",
                style = MaterialTheme.typography.headlineSmall.copy(color = Color.White)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Email: ${user.email}",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Name: ${user.name}",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Description: ${user.description}",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Birthdate: ${user.birthdate}",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Buttons section with consistent styling
            DefaultButton(
                s = "Edit Profile",
                onClick = { navController.navigate("UpdateProfilePage") },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            DefaultButton(
                s = "Sign Out",
                onClick = {
                    coroutineScope.launch {
                        signOutStatus = "Signing out..."
                        when (val result = authServices.signOut()) {
                            is com.tourpal.services.auth.Result.Failure -> {
                                signOutStatus = "Sign out failed: ${result.exception.message}"
                            }
                            is com.tourpal.services.auth.Result.Success<*> -> {
                                signOutStatus = "Signed out successfully"
                                onSignOutSuccess()
                            }
                        }
                    }
                },
                enabled = currentUser != null,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            DefaultButton(
                s = "Back",
                onClick = { navController.navigate("roleSelectionPage") },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            signOutStatus?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
