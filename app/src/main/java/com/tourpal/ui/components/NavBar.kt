package com.tourpal.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tourpal.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.tourpal.ui.theme.TourPalTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

data class NavItem(val label: String, val icon: Int, val selectedIcon: Int, val route: String)

val bottomNavItems = listOf(
    NavItem(
        "Profile", R.drawable.outline_person_24, R.drawable.baseline_person_24, "profilePage"
    ),
    NavItem(
        "Explore", R.drawable.outline_explore_24, R.drawable.baseline_explore_24, "searchToursPage"
    ),
    NavItem(
        "History", R.drawable.outline_view_timeline_24, R.drawable.baseline_view_timeline_24, "historyPage"
    ),
    NavItem(
        "Messages", R.drawable.outline_message_24, R.drawable.baseline_message_24, "mapPage"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showDialog = remember { mutableStateOf(false) }
    // Display the "Still working on it" AlertDialog when needed
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Still working on it") },
            confirmButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("OK")
                }
            }
        )
    }


    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .height(72.dp) // Increased height to accommodate content7

    ) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth() // Ensure the Column takes the full width of the item
                            .height(72.dp) // Match the height of the NavigationBar for vertical centering
                            .padding(vertical = 6.dp), // Reduced padding to give more space

                        horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally


                        verticalArrangement = Arrangement.Center // Center vertically
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (currentRoute == item.route) item.selectedIcon else item.icon
                            ),
                            contentDescription = item.label,
                            modifier = Modifier
                                .size(28.dp) // Reduced icon size to fit better
                        )
                        Text(
                            text = item.label,
                            modifier = Modifier.padding(top = 2.dp) // Small padding betwaeen icon and label
                        )
                    }
                },
                label = { }, // Empty label since we're including it in the icon Column
                selected = currentRoute == item.route,
                onClick = {
                    if (item.label == "History" || item.label == "Messages") {
                        showDialog.value = true
                    } else{
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }


                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavBarPreview() {
    val navController = rememberNavController()

    TourPalTheme {
        NavBar(navController = navController)
    }
}