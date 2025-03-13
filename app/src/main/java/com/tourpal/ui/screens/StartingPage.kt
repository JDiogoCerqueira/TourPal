package com.tourpal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tourpal.R

@Composable
fun StartingPage(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.tourpal_logo), // Replace with your logo resource
            contentDescription = "TourPal Logo"
        )
        Button(
            onClick = { navController.navigate("login") },
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