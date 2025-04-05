package com.tourpal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tourpal.data.model.repository.UserRepository
import com.tourpal.services.auth.AuthenticationServiceProvider
import com.tourpal.ui.components.BasicTextInput
import com.tourpal.ui.components.DefaultButton
import com.tourpal.ui.components.PasswordInput
import com.tourpal.ui.components.TourPalLogo
import com.tourpal.ui.viewmodels.SignupViewModel
import com.tourpal.ui.viewmodels.SignupViewModelFactory
import com.tourpal.services.auth.isSuccess
import com.tourpal.services.auth.isFailure
import com.tourpal.services.auth.exception
import com.tourpal.services.firestore.FirestoreService
import kotlinx.coroutines.launch

@Composable
fun SignUpPage(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Initialize ViewModel
    val authService = AuthenticationServiceProvider.provideAuthenticationService(context)
    val userRepository = UserRepository(FirestoreService()) // Initialize UserRepository
    val signupViewModel: SignupViewModel = viewModel(factory = SignupViewModelFactory(authService, userRepository))
    //val signupViewModel: SignupViewModel = viewModel(factory = SignupViewModelFactory(authService))
    val signupState = signupViewModel.signupResult


    // State variables
    var realName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordMismatchError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TourPalLogo(size = 300, text = false) // TourPal Logo
        Spacer(modifier = Modifier.height(24.dp))

        // Real Name Input
        BasicTextInput(
            placeholder = "Real Name",
            value = realName,
            onValueChange = { realName = it },
            modifier = Modifier.fillMaxWidth(0.9f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Email Input
        BasicTextInput(
            placeholder = "Email",
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(0.9f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            height = 150.dp
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Password Input
        PasswordInput(
            placeholder = "Password",
            value = password,
            onValueChange = {
                password = it
                passwordMismatchError = password != confirmPassword // Update error state
            },
            modifier = Modifier.fillMaxWidth(0.9f),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                autoCorrect = false
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Input
        PasswordInput(
            placeholder = "Confirm Password",
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                passwordMismatchError = password != confirmPassword // Update error state
            },
            modifier = Modifier.fillMaxWidth(0.9f),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                autoCorrect = false
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Password Mismatch Error
        if (passwordMismatchError && confirmPassword.isNotEmpty()) {
            Text(
                text = "Passwords do not match",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Start).padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Button
        DefaultButton(
            "Create Account",
            {
                if (password == confirmPassword) {
                    scope.launch {
                        signupViewModel.realName = realName
                        signupViewModel.email = email
                        signupViewModel.password = password
                        signupViewModel.signUp()
                    }
                } else {
                    passwordMismatchError = true
                }
            },
            Modifier.fillMaxWidth(0.8f)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Back to Login Button
        TextButton(
            onClick = { navController.navigate("loginPage") },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Already have an account? Log in", color = Color(0xFF0087FF))
        }
        Spacer(modifier = Modifier.weight(1f))

        // Loading Indicator
        if (signupViewModel.isLoading) {
            Popup(onDismissRequest = { /* Do nothing */ }) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFF0087FF)
                )
            }
        }

        // Error Handling
        signupState?.let { result ->
            if (result.isFailure()) {
                Text("Error: ${result.exception.message}", color = Color.Red, fontSize = 14.sp)
            }
        }

        // Success Navigation
        if (signupState?.isSuccess() == true) {
            LaunchedEffect(Unit) {
                navController.navigate("roleSelectionPage") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        }
    }
}