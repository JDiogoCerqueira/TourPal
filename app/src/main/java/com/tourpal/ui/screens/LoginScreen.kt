package com.tourpal.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tourpal.R
import com.tourpal.ui.viewmodels.LoginViewModel

@Composable
fun LoginScreen(navController: NavController) {
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
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.tourpal_logo),
            contentDescription = "TourPal Logo"
        )

        OutlinedButton(
            onClick = { viewModel.signInWithGoogle(context) },
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = "Google Logo",
                modifier = Modifier.padding(end = 8.dp).size(24.dp)
            )
            Text("Continue with Google")
        }

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username or Email") },
            modifier = Modifier.padding(8.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Your Password") },
            modifier = Modifier.padding(8.dp)
        )

        Button(
            onClick = { viewModel.signInWithEmail(username, password) },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Login")
        }

        Button(
            onClick = { navController.navigate("signin") },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Create Account")
        }

        if (state.isLoading) {
            Text("Loading...")
        }
        if (state.error != null) {
            Text("Error: ${state.error}")
        }
        if (state.user != null) {
            navController.navigate("chooseMode")
        }
    }
}