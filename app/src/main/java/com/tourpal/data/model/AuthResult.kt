package com.tourpal.data.model

sealed class AuthResult<out T> {
    data class Success<out T>(val data: T) : AuthResult<T>()
    data class Failure(val exception: Exception) : AuthResult<Nothing>()
}
