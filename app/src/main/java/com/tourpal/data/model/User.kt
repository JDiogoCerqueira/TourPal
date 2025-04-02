package com.tourpal.data.model

import com.google.firebase.firestore.PropertyName

data class User(
    @PropertyName("id") val id: String = "",
    @PropertyName("email") val email: String = "",
    @PropertyName("name") val name: String = "Unknown",
    @PropertyName("profilePhoto") val profilePhoto: String = ""
)
