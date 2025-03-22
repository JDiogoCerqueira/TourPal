package com.tourpal.data.model

import com.google.firebase.Timestamp

data class TourPlan(
    val id: String = "",
    val city: String = "",
    val createdAt: Timestamp? = null,
    val creatorId: String = "",
    val description: String = "",
    val title: String = "",
    val destinations: List<Destination> = emptyList(),
    val image: String = ""
)