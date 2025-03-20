package com.tourpal.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Popup
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tourpal.ui.components.*
import com.tourpal.ui.viewmodels.LoginViewModel

val buttonModifier = Modifier.fillMaxWidth(0.8f)

@Composable
fun LoginPage(navController: NavController) {
    val context = LocalContext.current
    val viewModel = remember { LoginViewModel.create(context) }
    val state = viewModel.state.value

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Launcher for Credential Manager (needed for Google Sign-In)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            viewModel.signInWithGoogle(context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TourPalLogo(size = 300) // TourPal Logo
        Spacer(modifier = Modifier.height(24.dp))
        GoogleSignInButton(
            onClick = { viewModel.signInWithGoogle(context) },
        )
        Spacer(modifier = Modifier.height(24.dp))

        BasicTextInput("User name or email", username, { username = it }, modifier = Modifier.fillMaxWidth(0.9f))
        Spacer(modifier = Modifier.height(16.dp))
        PasswordInput("Password", password, { password = it }, modifier = Modifier.fillMaxWidth(0.9f))
        Spacer(modifier = Modifier.height(24.dp))

        DefaultButton("Login", {}, buttonModifier) // Login Button
        Spacer(modifier = Modifier.height(16.dp))

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

        if(state.isLoading) {
            Popup(
                onDismissRequest = { /*TODO*/ }
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFF0087FF)
                )
            }
        }

        if (state.error != null) {
            Text("Error: ${state.error}")
        }
        if (state.user != null) {
            navController.navigate("roleSelectionPage")
        }
    }
}