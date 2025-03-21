package com.tourpal.ui.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.tourpal.ui.theme.TourPalTheme

data class NavItem(val label: String, val icon: Int, val selectedIcon:Int, val route: String)

val bottomNavItems = listOf(
    NavItem(
        "Profile", R.drawable.outline_person_24, R.drawable.baseline_person_24,"profilePage"
    ),
    NavItem(
        "Explore", R.drawable.outline_explore_24, R.drawable.baseline_explore_24,"searchToursPage"
    ),
    NavItem(
        "History", R.drawable.outline_view_timeline_24, R.drawable.baseline_view_timeline_24,"historyPage"
    ),
    NavItem(
        "Messages", R.drawable.outline_message_24, R.drawable.baseline_message_24, "messagesPage")
)

@Composable
fun NavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .height(64.dp)
    ) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Column {
                        Icon(
                            painter = painterResource(
                                id = if (currentRoute == item.route) item.selectedIcon else item.icon
                            ),
                            contentDescription = item.label,
                            modifier = Modifier.size(24.dp).padding(top = 4.dp)
                        )
                    }
                },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
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

