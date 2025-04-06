package com.tourpal.data.model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

data class TourPlanRating(
    @PropertyName("id") val id: String = "",
    @PropertyName("creatorid") val creatorRef: DocumentReference? = null,
    @PropertyName("ratingscore") val ratingScore: Int = 0,
    @PropertyName("reviewtext") val reviewText: String = "",
    @PropertyName("tourplanid") val tourPlanRef: DocumentReference? = null,
    @PropertyName("tourplanid_str") val tourPlanPath: String = ""

)