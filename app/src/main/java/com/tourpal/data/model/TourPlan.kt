package com.tourpal.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class TourPlan(
    @PropertyName("id") var id: String = "",
    @PropertyName("city") var city: String = "",
    @PropertyName("createdAt") var createdAt: Timestamp? = null,
    @PropertyName("creatorId") var creatorId: String = "",
    @PropertyName("description") var description: String = "",
    @PropertyName("title") var title: String = "",
    @PropertyName("destination") var destinations: List<Destination> = emptyList(),
    @PropertyName("image") var image: String = "",
    @PropertyName("destinations_count") var destinationsCount: Int = 0
) {
    val normalizedCity: String
        get() = city.trim().lowercase()
}