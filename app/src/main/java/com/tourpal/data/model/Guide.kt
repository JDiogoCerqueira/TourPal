package com.tourpal.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Guide(
    @PropertyName("id") val id: String = "" ,
    @PropertyName("userid")  val userid: String = "",
    @PropertyName("tourplanid") val tourplanid: String = "",
    @PropertyName("availability") val availability: List<Timestamp> = emptyList(),
    @PropertyName("ratingcount") val ratingcount: Int = 0,
    @PropertyName("ratingmean") val rating: Int = 0
)