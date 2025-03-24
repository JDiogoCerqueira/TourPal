package com.tourpal.data.model


import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import android.util.Log

object TourPlanRepository {
    private val firestore = FirebaseFirestore.getInstance()

    // Fetch tour plans filtered by city (real-time updates)
    fun getTourPlansByCity(city: String): Flow<List<TourPlan>> = callbackFlow {
        val query = firestore.collection("tourplan").whereEqualTo("city", city)
        val listenerRegistration: ListenerRegistration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val tourPlans = snapshot?.documents?.mapNotNull { doc ->
                // Convert document to TourPlan
                doc.toObject(TourPlan::class.java)
            } ?: emptyList()
            trySend(tourPlans).isSuccess
        }
        awaitClose { listenerRegistration.remove() }
    }

    // Fetch ratings for a specific tour plan and compute the average rating and review count
    suspend fun getRatingForTourPlan(tourPlanId: String): Pair<Float, Int> {
        return try {
            // Get the DocumentReference for the tourplan
            val tourPlanRef = firestore.collection("tourplan").document(tourPlanId)


            Log.d("Debug", "tourPlanId: $tourPlanId")
            Log.d("Debug", "tourPlanRef: $tourPlanRef")
            Log.d("Debug", "tourPlanRef.path: ${tourPlanRef.path}")


            // Query Firestore to get all ratings for this tour plan
            val snapshot = firestore.collection("tourplanrating")
                .whereEqualTo("tourplanid", tourPlanRef)
                .get()
                .await()


            Log.d("Debug", "Documents found: ${snapshot.documents.size}")

            // Extract rating scores and count the reviews
            val ratings = snapshot.documents.mapNotNull { doc ->
                doc.toObject(TourPlanRating::class.java)?.ratingScore
            }

            // Calculate the average rating and review count
            val reviewsCount = ratings.size
            val avgRating = if (reviewsCount > 0) ratings.sum().toFloat() / reviewsCount else 0f

            avgRating to reviewsCount
        } catch (e: Exception) {
            // Log error
            Log.e("TourPlanRating", "Error fetching ratings", e)
            0f to 0
        }
    }
}