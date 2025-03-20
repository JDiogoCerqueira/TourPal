package com.tourpal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.tourpal.navigation.NavGraph
import com.tourpal.ui.theme.TourPalTheme
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Make the status bar transparent
        enableEdgeToEdge()

        setContent {    // Set the content of the activity; in this case, the navigation graph
            TourPalTheme {  // Apply the theme to the content; it will cascade to all the composables in the hierarchy
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}