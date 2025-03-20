package com.tourpal.data.model

data class Tour(
    val title: String,
    val subtitle: String,
    val rating: Float,
    val reviews: Int,
    val duration: String,
    val destinations: Int,
    val isFavorite: Boolean
) // TODO: adjust for actual database data