package com.tourpal.data.model

import com.google.firebase.Timestamp

data class Availability(
    val endTime: Timestamp? = null,
    val startTime: Timestamp? = null,
    val status: Boolean = true,
    val tourPlanId: String = "",
    val userid: String = ""
)