package com.tourpal.data.model

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.GeoPoint

data class Destination(
    @PropertyName("coordinates") val coordinates: GeoPoint? = null,
    @PropertyName("description") val description: String = "",
    @PropertyName("name") val name: String = "",
    @PropertyName("imageURL") val imageURL: String = ""
)