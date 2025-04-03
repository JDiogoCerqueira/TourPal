package com.tourpal.ui.screens

import TopBar
//import android.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.tourpal.data.model.User
import com.tourpal.ui.components.BasicTextInput
import com.tourpal.ui.components.DatePickerModal
import com.tourpal.ui.components.DefaultButton
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.tourpal.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.MaterialTheme

@Composable
fun UpdateProfilePage(
    navController: NavHostController,
    getUser: suspend (String) -> User?,
    updateUser: suspend (String, User) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val currentUser = FirebaseAuth.getInstance().currentUser
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    var username by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var profilePhoto by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    // Calculate the maximum allowed birthdate (18 years ago)
    val maxBirthDate = remember {
        Calendar.getInstance().apply {
            add(Calendar.YEAR, -18)
        }.timeInMillis
    }


    // Fetch user data when screen loads or when currentUser changes
    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { userId ->
            isLoading = true
            try {
                val user = getUser(userId)
                user?.let {
                    username = it.name
                    description = it.description
                    birthdate = it.birthdate
                    profilePhoto = it.profilePhoto // Load profile photo URL
                }
            } catch (e: Exception) {
                errorMessage = "Failed to load user data: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }


    // Function to update the birthdate when the date is selected
    val onDateSelected: (Long?) -> Unit = { selectedDate ->
        selectedDate?.let {
            val currentDate = Calendar.getInstance().timeInMillis
            if (it > currentDate) {
                errorMessage = "Birthdate cannot be in the future"
            } else {
                // Format the selected date
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                birthdate = dateFormat.format(Date(it))
                errorMessage = null // Clear any previous error
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Profile photo container with consistent styling
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                if (profilePhoto.isNotEmpty()) {
                    AsyncImage(
                        model = profilePhoto,
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
                    )
                }
            }

            DefaultButton(
                s = "Change Photo",
                onClick = { /* TODO: Implement photo change */ },
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Username input
        BasicTextInput("Username", username, { username = it })
        Spacer(modifier = Modifier.height(16.dp))

        // Birthdate input as a clickable TextField
        DefaultButton(s = "Birthdate: ${if (birthdate.isNotEmpty()) birthdate else "Select a date"}",
            onClick = { showDatePicker = true },
            modifier = Modifier.padding(16.dp)


        )

        // Show Date Picker Modal when required
        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { date ->
                    onDateSelected(date)
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false },
                maxDate = Calendar.getInstance().timeInMillis // Prevent future dates
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description input
        BasicTextInput("Description", description, { description = it }, height = 150.dp)
        Spacer(modifier = Modifier.height(16.dp))




        // Function to handle profile update
        fun updateProfile() {
            if (currentUser == null) {
                errorMessage = "No user logged in"
                return
            }

            coroutineScope.launch {
                isLoading = true
                errorMessage = null
                successMessage = null

                try {
                    val updatedUser = User(
                        id = currentUser.uid,
                        email = currentUser.email ?: "",
                        name = username,
                        description = description,
                        birthdate = birthdate,
                        profilePhoto = profilePhoto
                    )

                    updateUser(currentUser.uid, updatedUser)
                    successMessage = "Profile updated successfully!"
                    navController.navigate("profilePage")
                } catch (e: Exception) {
                    errorMessage = "Failed to update profile: ${e.message}"
                } finally {
                    isLoading = false
                }
            }
        }



        // OK button (action to save the profile, etc.)
        DefaultButton(
            s = "Update Profile",
            onClick = { updateProfile() },
            enabled = !isLoading
        )


        DefaultButton(
            s = "Back",
            onClick = {
                navController.navigate("profilePage")
            },
            modifier = Modifier.fillMaxWidth(0.7f).padding(horizontal = 32.dp)
        )
    }
}