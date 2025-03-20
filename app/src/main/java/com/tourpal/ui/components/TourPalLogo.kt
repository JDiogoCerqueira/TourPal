package com.tourpal.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import com.tourpal.R

@Composable
fun TourPalLogo(modifier: Modifier = Modifier, size: Int = 150) {
    Box(
        modifier = modifier
            .size(size.dp) // Set the size of the Box
            .clip(RoundedCornerShape(16.dp)), // Optional: Apply rounded corners
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.tourpal_logo),
            contentDescription = "TourPal Logo",
            modifier = Modifier
                .fillMaxSize() // Fill the Box size
                .clip(RoundedCornerShape(16.dp)) // Clip the image to the same shape
                .scale(1.55f) // Scale the image to crop the transparent area
                .align(Alignment.Center) // Center the image
        )
        Text(
            text = "TourPal",
            fontSize = (size * 0.08).sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Default,
            color = Color(0xFFDCDCDC),
            modifier = Modifier
                .padding(8.dp)
                .offset(y = (size * 0.45).dp)
        )
    }
}


@Preview
@Composable
fun TourPalLogoPreview() {
    TourPalLogo()
}
