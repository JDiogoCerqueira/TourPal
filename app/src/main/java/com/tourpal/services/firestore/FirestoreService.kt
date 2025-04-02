package com.tourpal.services.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.tourpal.data.model.User

class FirestoreService {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()  // FirebaseAuth instance to get the current user

    // Save user data to Firestore
    suspend fun saveUser(user: User) {
        try {
            firestore.collection("user").document(user.id).set(user).await()
        } catch (e: Exception) {
            throw e
        }
    }

    // Get user data from Firestore
    suspend fun getUser(userId: String = auth.currentUser?.uid ?: ""): User? {

        if (userId.isEmpty()) return null  // If userId is empty, return null

        return try {
            val document = firestore.collection("user").document(userId).get().await()
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
            firestore.collection("user").document(userId).set(user).await()
        } catch (e: Exception) {
            throw e
        }
    }
}