package com.tourpal.data.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val profilePictureUrl: String? = null
) // TODO: adjust for actual database data