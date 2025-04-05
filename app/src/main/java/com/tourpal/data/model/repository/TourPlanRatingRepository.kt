package com.tourpal.data.model.repository

import com.tourpal.data.model.GuideRating
import com.tourpal.data.model.TourPlanRating
import com.tourpal.services.firestore.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TourPlanRatingRepository(private val firestoreService: FirestoreService) {

    // Save a TourPlanRating to Firestore
    suspend fun saveTourPlanRating(rating: TourPlanRating): Flow<Result<Unit>> = flow {
        try {
            firestoreService.saveTourPlanRating(rating)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    // Get a TourPlanRating by ID
    suspend fun getTourPlanRating(id: String): Flow<Result<TourPlanRating?>> = flow {
        try {
            val rating = firestoreService.getTourPlanRating(id)
            emit(Result.success(rating))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    // Update an existing TourPlanRating
    suspend fun updateTourPlanRating(rating: TourPlanRating): Flow<Result<Unit>> = flow {
        try {
            firestoreService.updateTourPlanRating(rating)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    // Get all TourPlanRatings for a specific tourplanId
    suspend fun getTourPlanRatingsByTourPlanId(tourPlanId: String): Flow<Result<List<GuideRating>>> = flow {
        try {
            val ratings = firestoreService.getTourplanRatingsByTourPlan(tourPlanId)
            emit(Result.success(ratings))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
