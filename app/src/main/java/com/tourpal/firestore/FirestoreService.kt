package com.tourpal.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.tourpal.services.auth.User
import kotlinx.coroutines.tasks.await

class FirestoreService {

    private val firestore = FirebaseFirestore.getInstance()

    // Save user data to Firestore
    suspend fun saveUser(user: User) {
        try {
            firestore.collection("users").document(user.id).set(user).await()
        } catch (e: Exception) {
            throw e
        }
    }

    // Get user data from Firestore
    suspend fun getUser(userId: String): User? {
        return try {
            val document = firestore.collection("users").document(userId).get().await()
            if (document.exists()) {
                val user = document.toObject(User::class.java)
                user
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    // Update user data in Firestore
    suspend fun updateUser(userId: String, user: User) {
        try {
            firestore.collection("users").document(userId).set(user).await()
        } catch (e: Exception) {
            throw e
        }
    }
}