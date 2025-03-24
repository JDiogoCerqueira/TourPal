package com.tourpal.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tourpal.data.model.TourPlan
import com.tourpal.data.model.TourPlanRepository

@Composable
fun TourPlanCard(
    tourPlan: TourPlan,
    // onClick can be used to navigate to a details page, for example.
    onClick: () -> Unit = {}
) {
    // Local state to hold the rating information (average rating, review count)
    var ratingInfo by remember { mutableStateOf(0f to 0) }
    // This effect fetches the ratings when the composable is launched or when the tourPlan changes.
    LaunchedEffect(key1 = tourPlan) {
        // Assuming that the TourPlan has an "id" property that represents its Firestore document ID.
        ratingInfo = TourPlanRepository.getRatingForTourPlan(tourPlanId = tourPlan.id)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Tour Image
            AsyncImage(
                model = tourPlan.image,
                contentDescription = "Tour Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tour title and description
            Text(
                text = tourPlan.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    color = Color.Black
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tourPlan.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    color = Color.Gray
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Row with rating and number of destinations
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Display the average rating and review count
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating Star",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFFFD700)
                    )
                    Text(
                        text = "${"%.1f".format(ratingInfo.first)} (${ratingInfo.second})",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 14.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                // Display the number of destinations
                Text(
                    text = "${tourPlan.destinations.size} destinations",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}