package com.tourpal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tourpal.ui.theme.TourPalTheme
import com.tourpal.ui.screens.StartingScreen
import com.tourpal.ui.screens.LoginScreen
import com.tourpal.ui.screens.SignupScreen
import com.tourpal.ui.screens.ChooseModeScreen
import androidx.compose.runtime.Composable

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
                        composable("starting") { StartingScreen(navController) }
                        composable("login") { LoginScreen(navController) }
                        composable("signup") { SignupScreen(navController) }
                        composable("chooseMode") { ChooseModeScreen(navController) }
                    }
                }
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun PreviewStartingScreen(){
    TourPalTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            StartingScreen(rememberNavController())
        }
    }
}
