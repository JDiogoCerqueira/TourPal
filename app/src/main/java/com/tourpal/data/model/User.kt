package com.tourpal.data.model

import com.google.firebase.Timestamp


data class User(
    val birthdate: Timestamp? = null,
    val description: String = "",
    val email: String = "",
    val nationality: String = "",
    val regDate: Timestamp? = null,
    val name: String = "",
    val profilePhoto: String = ""
)