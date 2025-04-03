package com.tourpal.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.tourpal.ui.components.*
import com.tourpal.ui.viewmodels.LoginViewModel
import kotlinx.coroutines.launch

val buttonModifier = Modifier.fillMaxWidth(0.8f)

@Composable
fun LoginPage(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Initialize ViewModel
    val loginViewModel = remember { LoginViewModel.create(context) }
    val loginState = loginViewModel.state.value

    // State variables
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Launcher for Google Sign-In
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            loginViewModel.signInWithGoogle(context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TourPalLogo(size = 300, text = false) // TourPal Logo
        Spacer(modifier = Modifier.height(24.dp))

        // Google Sign-In Button
        GoogleSignInButton(
            onClick = { loginViewModel.signInWithGoogle(context) },
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Email/Username and Password Inputs
        BasicTextInput(
            "User name or email",
            username,
            { username = it },
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        PasswordInput("Password", password, { password = it }, modifier = Modifier.fillMaxWidth(0.9f))
        Spacer(modifier = Modifier.height(24.dp))

        // Login Button
        DefaultButton(
            "Login",
            {
                scope.launch {
                    loginViewModel.signInWithEmail(username, password)
                }
            },
            buttonModifier
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Create Account Button
        Text(
            text = "Still don't have an account?",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.8f),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFA9A9A9)
        )
        DefaultButton(
            "Create Account",
            { navController.navigate("signUpPage") },
            buttonModifier
        )
        Spacer(modifier = Modifier.weight(1f))

        // Loading Indicator
        if (loginState.isLoading) {
            Popup(onDismissRequest = { /* Do nothing */ }) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFF0087FF)
                )
            }
        }

        // Error Handling
        loginState.error?.let { error ->
            Text("Error: $error", color = Color.Red, fontSize = 14.sp)
        }

        // Success Navigation
        if (loginState.user != null) {
            LaunchedEffect(Unit) {
                navController.navigate("roleSelectionPage") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        }
    }
}