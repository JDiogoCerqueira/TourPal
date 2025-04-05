package com.tourpal.data.model

import com.google.firebase.firestore.PropertyName

data class GuideRating(
    @PropertyName("id") val id: String = "",
    @PropertyName("creatorid") val creatorId: String = "",
    @PropertyName("guideid") val guideId: String = "",
    @PropertyName("ratingscore") val ratingScore: Int = 0,
    @PropertyName("reviewtext") val reviewText: String = ""
)