package com.tourpal.data.model


import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

// Each walked point stores both a coordinate and the time it was recorded.
data class WalkedPoint(
    val point: GeoPoint? = null,
    val recordedAt: Timestamp? = null
)

data class Log(
    val createdAt: Timestamp? = null,
    val endedAt: Timestamp? = null,
    val guideId: String = "",
    val images: List<String> = emptyList(),
    val notes: String = "",
    val tourPlanId: String = "",
    val userid: String = "",
    val walkedPath: List<WalkedPoint> = emptyList()
)