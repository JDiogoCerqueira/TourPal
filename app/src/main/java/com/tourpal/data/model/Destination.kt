package com.tourpal.data.model

import com.google.firebase.firestore.GeoPoint

data class Destination(
    val coordinates: GeoPoint? = null,
    val description: String = "",
    val name: String = ""
)