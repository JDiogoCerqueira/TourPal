package com.tourpal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tourpal.data.model.TourPlan
//import com.tourpal.data.model.TourPlanRepository
import com.tourpal.ui.components.TourPlanCard
import com.tourpal.ui.theme.TourPalTheme
import com.tourpal.data.model.repository.TourPlanRepository
import com.tourpal.services.firestore.FirestoreService


@Composable
fun TourPlansResultsPage(navController: NavHostController, query: String) {
    // Local state for the search text (pre-filled with the query)
    var searchText by remember { mutableStateOf(query) }

    val tourPlanRepository = remember {
        TourPlanRepository(FirestoreService())
    }
    // Process input: trim + lowercase
    val processedCity by remember {
        derivedStateOf {
            searchText.trim().lowercase()
        }
    }

    var tourPlans by remember { mutableStateOf(emptyList<TourPlan>()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(processedCity) {
        tourPlanRepository.getTourPlansByCity(processedCity).collect { result ->
            result.fold(
                onSuccess = { list ->
                    tourPlans = list
                    errorMessage = null
                },
                onFailure = { exception ->
                    errorMessage = exception.message
                    tourPlans = emptyList()
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Gray
                )
            },
            singleLine = true,

        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display a message if no tours are found
        if (tourPlans.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No tours found", fontSize = 18.sp)
            }
        } else {
            // Use a LazyColumn to display a list of TourPlanCard composables
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(tourPlans) { tourPlan ->
                    TourPlanCard(tourPlan = tourPlan, onClick = {
                        // Navigate to a tour details page using the tour plan's id.
                        navController.navigate("tour_details/${tourPlan.id}")
                    })
                }
            }
        }
    }
}
@Preview
@Composable
fun TourPlansResultsPagePreview() {
    TourPalTheme {
        TourPlansResultsPage(navController = rememberNavController(), query = "Aveiro")
    }
}
