package com.tourpal.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

private val DarkColorScheme = darkColorScheme(
    primary = Color(0x0F3F55FF), // Dark teal background
    secondary = Color(0xFFD4AF37), // Yellowish button color
    background = Color(0xFF1A2A44),
    surface = Color(0xFF1A2A44),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1A2A44), // Dark teal background
    secondary = Color(0xFFD4AF37), // Yellowish button color
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
    ),
)

@Composable
fun TourPalTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}