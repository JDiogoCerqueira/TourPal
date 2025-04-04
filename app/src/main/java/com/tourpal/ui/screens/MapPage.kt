package com.tourpal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tourpal.ui.components.DefaultButton
import com.tourpal.ui.components.MapComponent

@Composable
fun MapPage(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Location",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        MapComponent(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
        DefaultButton(
            s = "Back",
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 16.dp)
        )
    }
}