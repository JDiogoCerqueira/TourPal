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
fun ChooseModePage(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Continue as")
        Button(
            onClick = { /* Handle traveller mode */ },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Traveller")
        }
        Button(
            onClick = { /* Handle tour guide mode */ },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Tour Guide")
        }
    }
}