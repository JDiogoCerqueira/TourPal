package com.tourpal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.ui.unit.sp
import com.tourpal.R
import com.tourpal.ui.theme.TourPalTheme
import com.tourpal.ui.components.DefaultButton

@Composable
fun ShowAlertDialog(showDialog: Boolean, onDismiss: () -> Unit) {
  if (showDialog) {
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Still working on it") }, confirmButton = {Button(onClick = onDismiss) { Text("OK") } })
  }
}

@Composable
fun RoleSelectionPage(navController: NavHostController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val showDialog = remember { mutableStateOf(false) }
        ShowAlertDialog(showDialog.value) { showDialog.value = false }
        Image(
            painter = painterResource(id = R.drawable.tourpal_logo),
            contentDescription = "Tourpal Logo",
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Text(
            text = "🗺️ Select Your Role 🗺️",
            color = Color.White, // White for contrast against dark background
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 48.dp)
        )
        DefaultButton(
          onClick = {
              showDialog.value = true
          },
          s = "I'm a tourist",
          modifier = Modifier.fillMaxWidth(0.7f).padding(horizontal = 32.dp)
        )

        DefaultButton(
          onClick = {
              showDialog.value = true
          },
          s = "I'm a tour guide",
          modifier = Modifier.fillMaxWidth(0.7f).padding(horizontal = 32.dp).padding(top = 16.dp)
        )
    }


}

@Preview
@Composable
fun RoleSelectionPagePreview() {
    val navController = rememberNavController()
    TourPalTheme {
        RoleSelectionPage(navController = rememberNavController())
    }
}

