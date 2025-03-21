package com.tourpal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tourpal.ui.screens.*

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

        composable("searchToursPage") {
            SearchToursPage(navController)
        }

        composable("tourResultsPage/{query}") { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            TourResultsPage(navController, query)
        }
    }
}
