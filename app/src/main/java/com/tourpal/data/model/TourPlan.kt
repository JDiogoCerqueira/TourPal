package com.tourpal.data.model

import com.google.firebase.Timestamp

data class TourPlan(
    val city: String = "",
    val createdAt: Timestamp? = null,
    val creatorId: String = "",
    val description: String = "",
    val name: String = "",
    val destinations: List<Destination> = emptyList()
)