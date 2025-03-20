package com.tourpal.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tourpal.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun StartingPage(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.tourpal_logo),
            contentDescription = "TourPal Logo"
        )

        var isContentLoaded by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = isContentLoaded) {
            scope.launch {
                if (isContentLoaded) {
                    Log.d("StartingPage", "navigating to login")
                    delay(1500)
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                } else {
                    isContentLoaded = true
                }


            }
        }

    }
}

@Preview(showBackground = true, backgroundColor = 0x0F3F55FF)
@Composable
fun StartingPagePreview() {
    StartingPage(navController = rememberNavController())
}
