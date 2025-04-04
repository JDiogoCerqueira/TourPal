package com.tourpal.services.Storage

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.util.UUID

object StorageService {
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    suspend fun uploadProfileImage(fileUri: Uri): String? {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return null
        val imageRef: StorageReference = storageRef.child("profile_photos/$userId/${UUID.randomUUID()}")

        return try {
            imageRef.putFile(fileUri).await()
            imageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }
}
