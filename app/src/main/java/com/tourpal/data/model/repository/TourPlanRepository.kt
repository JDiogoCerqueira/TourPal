package com.tourpal.data.model.repository

import com.tourpal.data.model.TourPlan
import com.tourpal.services.firestore.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TourPlanRepository(private val firestoreService: FirestoreService) {

    // Save tourplan data to Firestore
    suspend fun saveTourPlan(tourplan: TourPlan): Flow<Result<Unit>> = flow {
        try {
            val normalizedTourPlan = tourplan.copy(city = tourplan.normalizedCity) // Use normalized version
            firestoreService.saveTourPlan(normalizedTourPlan)
            emit(Result.success(Unit)) // Emit success when tourplan is saved
        } catch (e: Exception) {
            emit(Result.failure(e)) // Emit failure with exception if an error occurs
        }
    }

    // Get tourplan data from Firestore by ID
    suspend fun getTourPlan(tourplanId: String): Flow<Result<TourPlan?>> = flow {
        try {
            val tourPlan = firestoreService.getTourPlan(tourplanId)
            emit(Result.success(tourPlan)) // Emit the result
        } catch (e: Exception) {
            emit(Result.failure(e)) // Emit failure if an error occurs
        }
    }

    // Update tourplan data in Firestore
    suspend fun updateTourPlan(tourplan: TourPlan): Flow<Result<Unit>> = flow {
        try {
            val normalizedTourPlan = tourplan.copy(city = tourplan.normalizedCity)// Use normalized version
            firestoreService.updateTourPlan(normalizedTourPlan)
            emit(Result.success(Unit)) // Emit success when tourplan is updated
        } catch (e: Exception) {
            emit(Result.failure(e)) // Emit failure with exception if an error occurs
        }
    }

    // Get tourplans by city
        suspend fun getTourPlansByCity(city: String): Flow<Result<List<TourPlan>>> = flow {
        try {
            val tourPlans = firestoreService.getTourPlansByCity(city)
            emit(Result.success(tourPlans)) // Emit success with list of tourplans
        } catch (e: Exception) {
            emit(Result.failure(e)) // Emit failure with exception if an error occurs
        }
    }

}