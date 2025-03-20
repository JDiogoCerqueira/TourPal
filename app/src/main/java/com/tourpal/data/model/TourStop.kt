package com.tourpal.data.model

// Stops at each tour
data class TourStop(
    val number: Int,
    val name: String,
    val isVisited: Boolean
) // TODO: adjust for actual database data