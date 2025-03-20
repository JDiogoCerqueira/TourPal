package com.tourpal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tourpal.ui.screens.StartingPage
import com.tourpal.ui.screens.LoginPage
import androidx.navigation.compose.rememberNavController
import com.tourpal.ui.screens.SignUpPage
import com.tourpal.ui.screens.RoleSelectionPage

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "loginPage") {
        composable("startingPage") {
            StartingPage(navController)
        }

        composable("loginPage") {
            LoginPage(navController)
        }

        composable("signUpPage") {
            SignUpPage(navController)
        }

        composable("roleSelectionPage") {
            RoleSelectionPage(navController)
        }
    }
}
