package com.tourpal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tourpal.data.model.repository.TourPlanRepository
import com.tourpal.services.firestore.FirestoreService
import com.tourpal.ui.components.MapComponent
import com.tourpal.ui.viewmodels.TourDetailsViewModel
import com.tourpal.ui.viewmodels.TourDetailsViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPage(navController: NavController, tourPlanId: String) {
    val viewModel: TourDetailsViewModel = viewModel(
        factory = TourDetailsViewModelFactory(TourPlanRepository(FirestoreService()))
    )
    val state by viewModel.state.collectAsState()

    LaunchedEffect(tourPlanId) {
        viewModel.loadTourPlan(tourPlanId)
        viewModel.getAllDestinations(tourPlanId)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top App Bar
            SmallTopAppBar(
                title = {
                    Text(
                        text = state.tourPlan?.title ?: "Tour Map",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
            
            // Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                when {
                    state.isLoading -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Loading tour map...")
                        }
                    }
                    state.error != null -> {
                        Card(
                            modifier = Modifier.align(Alignment.Center),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.Place,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = state.error ?: "Error loading map",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { navController.popBackStack() }
                                ) {
                                    Text("Go Back")
                                }
                            }
                        }
                    }
                    state.tourPlan != null -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            // Tour info card
                            if (state.tourPlan?.description?.isNotEmpty() == true) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                ) {
                                    Text(
                                        text = state.tourPlan?.description ?: "",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(16.dp),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            
                            // Map component
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                            ) {
                                MapComponent(
                                    modifier = Modifier.fillMaxSize(),
                                    destinations = state.tourPlan?.destinations ?: emptyList()
                                )
                                
                                // Destination counter badge
                                Card(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Place,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${state.tourPlan?.destinations?.size ?: 0} destinations",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    colors: TopAppBarColors
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = title,
        navigationIcon = navigationIcon,
        colors = colors)
}
