package com.tourpal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.tourpal.services.auth.AuthenticationServiceProvider
import com.tourpal.services.auth.AuthenticationServices
import com.tourpal.ui.screens.*
import com.tourpal.services.firestore.FirestoreService
import com.tourpal.data.model.repository.UserRepository
import com.tourpal.ui.viewmodels.UserViewModel

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    authServices: AuthenticationServices = AuthenticationServiceProvider.provideAuthenticationService(
        navController.context,
    ),
    firestoreService: FirestoreService = FirestoreService()
) {

    val userRepository = UserRepository(firestoreService)
    val userViewModel = UserViewModel(userRepository)

    val startDestination = if (FirebaseAuth.getInstance().currentUser != null)
        "roleSelectionPage"
    else
        "startingPage"

    NavHost(navController = navController, startDestination = startDestination) {
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

        composable("profilePage") {
            ProfileScreen(
                navController = navController,
                authServices = authServices,
                onSignOutSuccess = {
                    navController.navigate("loginPage") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                },
                userViewModel = userViewModel
            )
        }

        composable("mapPage/{tourPlanId}") { backStackEntry ->
            val tourPlanId = backStackEntry.arguments?.getString("tourPlanId") ?: ""
            MapPage(navController = navController, tourPlanId = tourPlanId)
        }

        composable("tourPlansResultsPage/{query}") { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            TourResultsPage(navController, query)
        }

        composable("tour_details/{tourPlanId}") { backStackEntry ->
            val tourPlanId = backStackEntry.arguments?.getString("tourPlanId") ?: ""
            TourDetailsPage(navController = navController, tourPlanId = tourPlanId)
        }



        composable("UpdateProfilePage") {
            UpdateProfilePage(
                navController = navController,
                userViewModel = userViewModel
            )
        }
    }
}