package com.tourpal.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp


val Typography = Typography(
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        color = TourPalColors.onBackground // Set text color for body
    ),
    // You can define more text styles here
)

val Shapes = Shapes(
    small = CircleShape,
    medium = RoundedCornerShape(16.dp),
    large = CutCornerShape(topEnd = 16.dp),
)

val gradientBrush = Brush.verticalGradient(
    colors = listOf(Color(0xFF2B806D), Color(0xFF095044))
)

@Composable
fun TourPalTheme(content: @Composable () -> Unit) {
    Box(modifier = Modifier.background(gradientBrush)) {
        MaterialTheme(
            colorScheme = TourPalColors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}
