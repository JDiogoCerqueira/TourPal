package com.tourpal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tourpal.R
import com.tourpal.ui.theme.TourPalTheme
import com.tourpal.ui.components.BasicTextInput
import com.tourpal.ui.components.PasswordInput

@Composable
fun SignUpPage(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Background with a gradient
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.tourpal_logo),
                contentDescription = "TourPal Logo",
                modifier = Modifier.size(120.dp) // Adjust size as needed
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary // Use your theme's onPrimary color
            )

            Spacer(modifier = Modifier.height(16.dp))

            BasicTextInput("Username", username, { username = it })
            Spacer(modifier = Modifier.height(16.dp))
            BasicTextInput("Email", email, { email = it })
            Spacer(modifier = Modifier.height(16.dp))
            PasswordInput("Password", password, { password = it })
            Spacer(modifier = Modifier.height(16.dp))
            PasswordInput("Confirm Password", confirmPassword, { confirmPassword = it })
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* Handle sign up */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Sign Up", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { navController.navigate("loginPage") },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(text = "Already have an account? Log in", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupPagePreview() {
    val navController = rememberNavController()
    TourPalTheme {
        SignUpPage(navController = navController)
    }
}
