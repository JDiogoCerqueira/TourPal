package com.tourpal.services.auth

// Extension functions for Result class
fun <T> Result<T>.isSuccess(): Boolean = this is Result.Success
fun <T> Result<T>.isFailure(): Boolean = this is Result.Failure
val <T> Result<T>.exception: Exception
    get() = (this as? Result.Failure)?.exception ?: Exception("Unknown error")
val <T> Result<T>.data: T?
    get() = (this as? Result.Success)?.data