package com.tourpal.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class TourPlan(
    @PropertyName("id") val id: String = "",
    @PropertyName("city") val city: String = "",
    @PropertyName("createdAt") val createdAt: Timestamp? = null,
    @PropertyName("creatorId") val creatorId: String = "",
    @PropertyName("description") val description: String = "",
    @PropertyName("title") val title: String = "",
    @PropertyName("destinations") val destinations: List<Destination> = emptyList(),
    @PropertyName("image") val image: String = ""
){
    // Normalized version of the city (trim + lowercase)
    val normalizedCity: String
        get() = city.trim().lowercase()
}