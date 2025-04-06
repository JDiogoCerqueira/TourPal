package com.tourpal.data.model

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.GeoPoint

data class Destination(
    @PropertyName("coordinates") var coordinates: GeoPoint? = null,
    @PropertyName("description") var description: String = "",
    @PropertyName("name") var name: String = "",
    @PropertyName("imageurl") var imageURL: String = ""
)