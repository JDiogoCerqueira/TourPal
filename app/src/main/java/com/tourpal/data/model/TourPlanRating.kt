package com.tourpal.data.model

import com.google.firebase.firestore.PropertyName

data class TourPlanRating(
    @PropertyName("id") val id: String = "",
    @PropertyName("creatorid") val creatorId: String = "",
    @PropertyName("ratingscore") val ratingScore: Int = 0,
    @PropertyName("reviewtext") val reviewText: String = "",
    @PropertyName("tourplanid") val tourPlanId: String = ""
)