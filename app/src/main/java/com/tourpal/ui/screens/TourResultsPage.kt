package com.tourpal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.tourpal.data.model.repository.TourPlanRatingRepository
import com.tourpal.data.model.repository.TourPlanRepository
import com.tourpal.services.firestore.FirestoreService
import com.tourpal.ui.theme.TourPalTheme
import com.tourpal.ui.viewmodels.TourResultsViewModel
import com.tourpal.ui.viewmodels.TourResultsViewModelFactory
import TopBar
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import com.tourpal.data.model.TourPlan

@Composable
fun TourResultsPage(navController: NavHostController, query: String) {
    val viewModel: TourResultsViewModel = viewModel(
        factory = TourResultsViewModelFactory(
            tourPlanRepository = TourPlanRepository(FirestoreService()),
            tourPlanRatingRepository = TourPlanRatingRepository(FirestoreService())
        )
    )
    val state by viewModel.state.collectAsState()

    LaunchedEffect(query) {
        viewModel.fetchTourPlansByCity(query)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(text = "Explore Tour Plans")

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar (pre-filled with the query)
        var searchText by remember { mutableStateOf(query) }
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp)),
            placeholder = {
                Text(
                    text = "Search",
                    color = Color.Gray
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Gray
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                cursorColor = Color.Gray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            keyboardActions = KeyboardActions(
                onSearch = {
                    navController.navigate("tourPlansResultsPage/$searchText")
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = state.error ?: "An error occurred",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red)
                )
            }
        } else if (state.tourPlans.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No tour plans found for \"$query\"",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
            }
        } else {
            LazyColumn {
                items(state.tourPlans) { tourPlan ->
                    TourPlanCard(
                        tourPlan = tourPlan,
                        averageRating = state.ratings[tourPlan.id] ?: 0.0,
                        onClick = {
                            navController.navigate("tour_details/${tourPlan.id}")
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun TourPlanCard(
    tourPlan: TourPlan,
    averageRating: Double,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(Color.Transparent),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Tour Image
            Image(
                painter = rememberAsyncImagePainter(model = tourPlan.image),
                contentDescription = "Tour Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tour Name and Favorite Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = tourPlan.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                )
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite Icon",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Rating
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating Star",
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFFFFD700) // Gold color for the star
                )
                Text(
                    text = String.format("%.1f", averageRating),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 14.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Number of Destinations
            Text(
                text = "${tourPlan.destinations.size} destinations",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            )
        }
    }
}
