package com.tourpal.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color.Companion.LightGray

//Main Colors for the app
val DarkTeal = Color(0xFF1A3C34)
val DarkGray = Color(0xFF1E1E1E)
val Lavender = Color(0xFF2B806D)
val DarkGray2 = Color(0xFF262626)
val Gray3 = Color(0xFF4B4B4B)
val Gray4 = Color(0xFFD3D3D3)
val Gray5 = Color(0xFFF5F5F5)

//Colors for other elements in the app
val DarkBlue1 = Color(0xFF003366)
val DarkBlue2 = Color(0xFF006699)
val LightBlue = Color(0xFFADD8E6)
val White = Color.White
val Black = Color.Black
val Gray = Color.Gray
val Red = Color.Red
val Yellow = Color.Yellow

val TourPalColors = lightColorScheme(
    background = DarkTeal,
    surface = DarkGray,
    primary = Lavender,
    secondary = DarkGray2,
    onBackground = Gray3,
    onSurface = Gray4,
    onPrimary = Gray5,
    onSecondary = Gray4,
    outline = LightGray
)