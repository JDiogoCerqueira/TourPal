package com.tourpal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tourpal.ui.theme.TourPalTheme
import com.tourpal.ui.screens.StartingPage
import com.tourpal.ui.screens.LoginPage
import com.tourpal.ui.screens.SigninPage
import com.tourpal.ui.screens.ChooseModePage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TourPalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "starting") {
                        composable("starting") { StartingPage(navController) }
                        composable("login") { LoginPage(navController) }
                        composable("signin") { SigninPage(navController) }
                        composable("chooseMode") { ChooseModePage(navController) }
                    }
                }
            }
        }

    }

}