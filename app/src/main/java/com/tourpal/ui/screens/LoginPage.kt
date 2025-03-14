package com.tourpal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tourpal.R

@Composable
fun LoginPage(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.tourpal_logo),
            contentDescription = "TourPal Logo"
        )
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
            onClick = { /* Handle login logic */ },
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
    }
}

@Preview()
@Composable
fun LoginPagePreview() {
    LoginPage(navController = NavController(context = androidx.compose.ui.platform.LocalContext.current))
}