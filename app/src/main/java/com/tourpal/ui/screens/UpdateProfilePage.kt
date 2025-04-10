package com.tourpal.ui.screens

import TopBar
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
import com.google.firebase.auth.FirebaseAuth
import com.tourpal.data.model.User
import com.tourpal.ui.components.BasicTextInput
import com.tourpal.ui.components.DatePickerModal
import com.tourpal.ui.components.DefaultButton
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.Image
import com.tourpal.R
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.MaterialTheme
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.collectAsState
import com.tourpal.data.model.repository.UserRepository
import com.tourpal.services.Storage.StorageService
import com.tourpal.ui.viewmodels.UserViewModel



@Composable
fun UpdateProfilePage(
    navController: NavHostController,
    userViewModel: UserViewModel
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

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var uploadProgress by remember { mutableStateOf<Float?>(null) }

    // Observe user data from the ViewModel
    val userState by userViewModel.user.collectAsState()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                imageUri = it
                coroutineScope.launch {
                    uploadProgress = 0.2f
                    val downloadUrl = StorageService.uploadProfileImage(it)
                    if (downloadUrl != null) {
                        profilePhoto = downloadUrl
                    } else {
                        errorMessage = "Image upload failed"
                    }
                    uploadProgress = null
                }
            }
        }
    )

    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { userId ->
            isLoading = true
            try {
                userViewModel.loadUser(userId)  // Fetch user data using ViewModel
            } catch (e: Exception) {
                errorMessage = "Failed to load user data: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    // When the user is loaded, populate the fields
    LaunchedEffect(userState) {
        userState?.let {
            username = it.name
            description = it.description
            birthdate = it.birthdate
            profilePhoto = it.profilePhoto // Load profile photo URL
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
        TopBar("Edit my Profile")

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
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
                        modifier = Modifier.size(60.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }

                // Upload progress indicator
                uploadProgress?.let { progress ->
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                    )
                }
            }

            DefaultButton(
                s = "Change Photo",
                onClick = { imagePicker.launch("image/*") },
                modifier = Modifier.padding(start = 16.dp),
                enabled = uploadProgress == null
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Username input
        BasicTextInput("Username", username, { username = it })
        Spacer(modifier = Modifier.height(16.dp))

        // Birthdate input as a clickable TextField
        DefaultButton(s = "Birthdate: ${if (birthdate.isNotEmpty()) birthdate else "Select a date"}",
            onClick = { showDatePicker = true },
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(48.dp)


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
        BasicTextInput("Description", description, { description = it })
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

                    //userRepository.updateUser(updatedUser)
                    userViewModel.updateUser(updatedUser)

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
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(48.dp)
        )

        // Spacer
        Spacer(modifier = Modifier.height(16.dp))

        DefaultButton(
            s = "Back",
            onClick = {
                navController.navigate("profilePage")
            }
            ,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(48.dp)
        )
    }
}