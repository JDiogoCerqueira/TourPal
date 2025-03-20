package com.tourpal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tourpal.data.model.TourStop
import com.tourpal.R

@Composable
fun TourStopItem(stop: TourStop) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(5f / 3f)
                    .background(Color.Gray, shape = RoundedCornerShape(4.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.aveiro1),
                    contentDescription = "Tour Stop Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = if (painterResource(id = R.drawable.aveiro1).intrinsicSize.width < painterResource(id = R.drawable.aveiro1).intrinsicSize.height) {
                        ContentScale.FillWidth
                    } else {
                        ContentScale.FillHeight
                    }
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp) // Size of the background container
                        .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(2.dp)), // Background for the checkbox
                    contentAlignment = Alignment.Center
                ) {
                    Checkbox(
                        checked = stop.isVisited,
                        onCheckedChange = {},
                        enabled = false
                    )
                }
            }
            Text(
                text = "${stop.number}. ${stop.name}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TourStopItemPreviewList() {
    MaterialTheme {
        Column {
            TourStopItem(TourStop(1, "Costa Nova Typical Houses", isVisited = false))
            TourStopItem(TourStop(2, "Aveiro City Center", isVisited = true))
            TourStopItem(TourStop(3, "Ria de Aveiro", isVisited = false))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TourStopItemPreview() {
    TourStopItem(TourStop(1, "Costa Nova Typical Houses", isVisited = false))
}