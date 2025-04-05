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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tourpal.data.model.repository.TourPlanRatingRepository
import com.tourpal.ui.components.TourPlanCard
import com.tourpal.data.model.repository.TourPlanRepository
import com.tourpal.services.firestore.FirestoreService
import com.tourpal.ui.viewmodels.TourPlanRatingViewModel
import com.tourpal.ui.viewmodels.TourPlanViewModel


@Composable
fun TourPlansResultsPage(navController: NavHostController, query: String) {
    // Local state for the search text (pre-filled with the query)
    var searchText by remember { mutableStateOf(query) }

    val tourplanViewModel: TourPlanViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TourPlanViewModel(TourPlanRepository(FirestoreService())) as T
            }
        }
    )

    val ratingViewModel: TourPlanRatingViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TourPlanRatingViewModel(TourPlanRatingRepository(FirestoreService())) as T
            }
        }
    )

    // Process input: trim + lowercase
    val processedCity by remember {
        derivedStateOf {
            searchText.trim().lowercase()
        }
    }

    LaunchedEffect(processedCity) {
        tourplanViewModel.loadTourPlansByCity(processedCity)
    }
    val tourPlansResult by tourplanViewModel.tourPlansState.collectAsState()
    val errorMessage by tourplanViewModel.errorMessage.collectAsState()

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

        when {
            errorMessage != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = errorMessage!!,
                        fontSize = 18.sp,
                        color = Color.Red
                    )
                }
            }
            tourPlansResult.isSuccess -> {
                val tourPlans = tourPlansResult.getOrNull() ?: emptyList()
                if (tourPlans.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No tours found", fontSize = 18.sp)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(tourPlans) { tourPlan ->
                            TourPlanCard(
                                tourPlan = tourPlan,
                                ratingViewModel = ratingViewModel,
                                onClick = {
                                    navController.navigate("tour_details/${tourPlan.id}")
                                }
                            )
                        }
                    }
                }
            }
            else -> {
                // Loading state or initial state
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Loading...", fontSize = 18.sp)
                }
            }
        }
    }
}
