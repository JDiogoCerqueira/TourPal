package com.tourpal.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class TourLog(
    val createdAt: Timestamp? = null,
    val endedAt: Timestamp? = null,
    val guideId: String? = null,
    val notes: String? = null,
    val tourPlanId: String? = null,
    val userId: String? = null,
    val logs: List<Annotation>? = null,
    val walkedPath: List<WalkedPoint>? = null
) {
    data class WalkedPoint(
        val point: GeoPoint? = null,
        val recordedAt: Timestamp? = null
    )

    data class Annotation(
        val logId: String? = null,
        val notes: String? = null,
        val images: List<Map<String, GeoPoint>>? = null
    )
}