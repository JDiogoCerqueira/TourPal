package com.tourpal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tourpal.R

@Composable
fun ChooseModePage(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.tourpal_logo),
            contentDescription = "Tourpal Logo",
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Button(
            onClick = { navController.navigate("some_destination") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
        ) {
            Text("Join", color = Color.White)
        }

        Button(
            onClick = { navController.navigate("some_destination") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp).padding(top = 16.dp)
        ) {
            Text("Host", color = Color.White)
        }
    }
}

@Preview
@Composable
fun ChooseModePagePreview() {
    ChooseModePage(navController = rememberNavController())
}
