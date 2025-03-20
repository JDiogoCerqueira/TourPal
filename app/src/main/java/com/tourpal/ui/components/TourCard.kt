package com.tourpal.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tourpal.data.model.Tour
import com.tourpal.ui.theme.*
import com.tourpal.R.drawable.aveiro1 // png image

@Composable
fun TourCard(tour: Tour) {
    Box(
        modifier = Modifier
            .fillMaxWidth().clip(RoundedCornerShape(16.dp))
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box (modifier = Modifier.clip(RoundedCornerShape(8.dp))) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Gray)
                        .size(180.dp), // This sets both width and height to 180.dp
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = aveiro1),
                        contentScale = ContentScale.Crop, // Changed to Crop to fill the Box
                        contentDescription = "Tour Image",
                        modifier = Modifier.fillMaxSize() // Ensure the Image fills the Box
                    )
                }
                Icon(
                    imageVector = if (tour.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (tour.isFavorite) Red else Gray,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .size(30.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Row(modifier = Modifier.padding(top = 8.dp).fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(tour.title, style = MaterialTheme.typography.bodyLarge)
                    Text(tour.subtitle, style = MaterialTheme.typography.bodyMedium, color = Gray)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${tour.duration} • ${tour.destinations} destinations", color = Gray, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Column(modifier = Modifier.wrapContentWidth(), horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = "Rating", tint = Yellow)
                        Text(text = "${tour.rating} (${tour.reviews})", color = Gray )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun TourCardPreview() {
    TourPalTheme {
        TourCard(
            tour = Tour(
                title = "Example Tour",
                subtitle = "Description of the tour",
                rating = 4.5f,
                reviews = 100,
                duration = "3 days",
                destinations = 5,
                isFavorite = true
            )
        )

    }
}
