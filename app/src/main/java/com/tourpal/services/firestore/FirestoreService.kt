package com.tourpal.services.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.tourpal.data.model.User
import  com.tourpal.data.model.TourPlan
import com.tourpal.data.model.GuideRating
import com.tourpal.data.model.TourPlanRating
import com.tourpal.data.model.Guide


class FirestoreService {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()


    ////USER////


    // Save user data to Firestore
    suspend fun saveUser(user: User) {
        try {
            firestore.collection("user").document(user.id).set(user).await()
        } catch (e: Exception) {
            throw e
        }
    }

    // Get user data from Firestore
    suspend fun getUser(userId: String): User? {
        return try {
            val document = firestore.collection("user").document(userId).get().await()
            if (document.exists()) {
                document.toObject(User::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }


    // Update user data in Firestore
    suspend fun updateUser( user: User) {
        try {
            val updates = mapOf(
                "name" to user.name,
                "description" to user.description,
                "birthdate" to user.birthdate,
                "profilePhoto" to user.profilePhoto
            )
            firestore.collection("user").document(user.id).update(updates).await()
        } catch (e: Exception) {
            throw Exception("Failed to update user: ${e.message}", e)
        }
    }



    ////TOURPLAN////

    // Save tourplan data to Firestore
    suspend fun saveTourPlan(tourplan: TourPlan) {
        try {
            firestore.collection("tourplan").document(tourplan.id).set(tourplan).await()
        } catch (e: Exception) {
            throw e
        }
    }

    // Get tourplan data from Firestore by ID
    suspend fun getTourPlan(tourplanId: String): TourPlan? {
        return try {
            val document = firestore.collection("tourplan").document(tourplanId).get().await()
            if (document.exists()) {
                document.toObject(TourPlan::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
            }
    }

    // Update tourplan data in Firestore
    suspend fun updateTourPlan(tourplan: TourPlan) {
        try {
            val updates = mapOf(
                "title" to tourplan.title,
                "description" to tourplan.description,
                "destinations" to tourplan.destinations,
                "image" to tourplan.image,
                "city" to tourplan.city
            )
            firestore.collection("tourplan").document(tourplan.id).update(updates).await()
        } catch (e: Exception) {
            throw Exception("Failed to update tourplan: ${e.message}", e)
        }
    }

    // Get tourplan data from Firestore filtered by city
    suspend fun getTourPlansByCity(city: String): List<TourPlan> {
        return try {
            val snapshot = firestore.collection("tourplan")
                .whereEqualTo("city", city) // Query by city field
                .get()
                .await()

            // Convert snapshot to a list of TourPlan objects
            snapshot.documents.mapNotNull { document ->
                document.toObject(TourPlan::class.java)
            }
        } catch (e: Exception) {
            emptyList() // Return an empty list if any error occurs
        }
    }



    ////GUIDERATING////

    // Save GuideRating data to Firestore
    suspend fun saveGuideRating(guiderating: GuideRating) {
        try {
            firestore.collection("guiderating").document(guiderating.id).set(guiderating).await()
        } catch (e: Exception) {
            throw Exception("Failed to save guide rating: ${e.message}", e)
        }
    }

    // Get a GuideRating by 'id'
    suspend fun getGuideRating(guideratingId: String): GuideRating? {
        if (guideratingId.isEmpty()) return null

        return try {
            val document = firestore.collection("guiderating").document(guideratingId).get().await()
            if (document.exists()) {
                document.toObject(GuideRating::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    // Update a GuideRating by 'id'
    suspend fun updateGuideRating(rating: GuideRating) {
        try {
            val updates = mapOf(
                "ratingscore" to rating.ratingScore,
                "reviewtext" to rating.reviewText
            )
            firestore.collection("guiderating").document(rating.id).update(updates).await()
        } catch (e: Exception) {
            throw Exception("Failed to update guide rating: ${e.message}", e)
        }
    }




    ////TOURPLANRATING////

    // Save a TourPlanRating to Firestore
    suspend fun saveTourPlanRating(tourplanrating: TourPlanRating) {
        try {
            firestore.collection("tourplanrating").document(tourplanrating.id).set(tourplanrating).await()
        } catch (e: Exception) {
            throw Exception("Failed to save tour plan rating: ${e.message}", e)
        }
    }

    // Get a TourPlanRating from Firestore
    suspend fun getTourPlanRating(tourplanratingId: String): TourPlanRating? {
        if (tourplanratingId.isEmpty()) return null

        return try {
            val document = firestore.collection("tourplanrating").document(tourplanratingId).get().await()
            if (document.exists()) {
                document.toObject(TourPlanRating::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }


    // Update an existing TourPlanRating
    suspend fun updateTourPlanRating(rating: TourPlanRating) {
        try {
            val updates = mapOf(
                "ratingscore" to rating.ratingScore,
                "reviewtext" to rating.reviewText
            )
            firestore.collection("tourplanrating").document(rating.id).update(updates).await()
        } catch (e: Exception) {
            throw Exception("Failed to update tour plan rating: ${e.message}", e)
        }
    }

    // Get Tourplan rating filtered by tourplanid
    suspend fun getTourplanRatingsByTourPlan(tourPlanId: String): List<GuideRating> {
        return try {
            val snapshot = firestore.collection("guiderating") // Assuming the ratings are stored in guideRatings collection
                .whereEqualTo("tourplanid", tourPlanId) // Filter ratings by the tourPlanId
                .get()
                .await()

            // Convert the snapshot to a list of GuideRating objects
            snapshot.documents.mapNotNull { document ->
                document.toObject(GuideRating::class.java)
            }
        } catch (e: Exception) {
            emptyList() // Return an empty list if any error occurs
        }
    }
    // Get the average rating for a tourplan by tourplanId
    suspend fun getAverageTourPlanRating(tourPlanId: String): Double {
        return try {
            val snapshot = firestore.collection("tourplanrating")
                .whereEqualTo("tourplanid", tourPlanId)
                .get()
                .await()

            val ratings = snapshot.documents.mapNotNull {
                it.toObject(TourPlanRating::class.java)?.ratingScore
            }

            if (ratings.isEmpty()) 0.0 else ratings.average()
        } catch (e: Exception) {
            0.0
        }
    }


    ////GUIDE////

    // Save a Guide to Firestore
    suspend fun saveGuide(guide: Guide) {
        try {
            firestore.collection("guide").document(guide.id).set(guide).await()
        } catch (e: Exception) {
            throw Exception("Failed to save guide: ${e.message}", e)
        }
    }

    // Get a Guide from Firestore by ID
    suspend fun getGuide(guideId: String): Guide? {
        if (guideId.isEmpty()) return null

        return try {
            val document = firestore.collection("guide").document(guideId).get().await()
            if (document.exists()) {
                document.toObject(Guide::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    // Update an existing Guide (e.g., availability, rating)
    suspend fun updateGuide(guide: Guide) {
        try {
            val updates = mapOf(
                "availability" to guide.availability,
                "ratingcount" to guide.ratingcount,
                "ratingmean" to guide.rating
            )
            firestore.collection("guide").document(guide.id).update(updates).await()
        } catch (e: Exception) {
            throw Exception("Failed to update guide: ${e.message}", e)
        }

    }



}