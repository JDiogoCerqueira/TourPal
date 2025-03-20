package com.tourpal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tourpal.data.model.Guide
import com.tourpal.ui.theme.*

@Composable
fun GuideCard(guide: Guide) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        // Profile Picture
        Box(
            modifier = Modifier
                .size(90.dp)
                .background(Gray),
            contentAlignment = Alignment.Center
        ) {
            Text("Pic", color = White)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.padding(0.dp, 4.dp, 4.dp, 4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(guide.name, style = MaterialTheme.typography.titleMedium)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "Rating", tint = Yellow)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${guide.rating} (${guide.reviews})", color = DarkGray2)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(guide.bio, color = Color.Gray)
        }
    }
}

@Preview
@Composable
fun GuideCardPreview() {
    val guide = Guide(
        name = "John Doe",
        rating = 4.5f,
        reviews = 100,
        bio = "Experienced guide with a passion for sustainable travel."
    )

    TourPalTheme {
        GuideCard(guide = guide)
    }

}


