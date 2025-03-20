package com.tourpal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.tourpal.ui.theme.TourPalTheme

@Composable
fun TourCard(
    tourName: String,
    rating: String,
    details: String,
    onFindGuideClick: () -> Unit,
    onSoloClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.padding(80.dp) // revert to 8dp
    ) {
        Column {
            Row(modifier = Modifier.padding(8.dp)) {
                // Thumbnail placeholder
                Box(modifier = Modifier.size(80.dp).background(Color.Gray))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(tourName, fontWeight = FontWeight.Bold)
                    Text(rating)
                    Text(details, color = Color.Gray)
                }
            }
            Row {
                DefaultButton("Find a Guide", onFindGuideClick)
                Spacer(modifier = Modifier.width(8.dp))
                DefaultButton("Solo Adventure", onSoloClick)
            }
        }
    }
}


@Preview
@Composable
fun TourCardPreview() {
    TourPalTheme {
        TourCard(
            tourName = "Awesome Tour",
            rating = "4.5/5",
            details = "This is a description of the tour.",
            onFindGuideClick = {}, onSoloClick = {},
        )

    }
}