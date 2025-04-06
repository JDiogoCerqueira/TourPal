package com.tourpal.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tourpal.R
import com.tourpal.data.model.Destination

@Composable
fun DestinationCard(
    modifier: Modifier = Modifier,
    destination: Destination
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
            modifier = Modifier
                .size(180.dp)
                .clip(RoundedCornerShape(16.dp))
            ) {
            Image(
                painter = rememberAsyncImagePainter(model = destination.imageURL),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                )
            )
            Text(
                text = destination.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
            )
            }
            Text(
            text = destination.description,
            fontSize = 14.sp,
            color = Color.Gray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.Start)
            )
        }
    }
}

@Preview
@Composable
fun DestinationCardPreview() {
    Column(
        modifier = Modifier.background(Color.DarkGray).padding(16.dp)
    ) {
        DestinationCard(
            destination = Destination(
                name = "Eiffel Tower",
                imageURL = "https://cdn.visitportugal.com/sites/default/files/styles/encontre_detalhe_poi_destaque/public/mediateca/Ilhavo%20-%20Praia%20Costa%20Nova_shutterstock_410111878_CN_Lukasz%20Janyst_660x371.jpg?itok=4GAWRFFM",
            )
        )
    }
}


