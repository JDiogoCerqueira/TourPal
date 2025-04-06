package com.tourpal.data.model.repository

import android.util.Log
import com.tourpal.data.model.Destination
import com.tourpal.data.model.TourPlan
import com.tourpal.services.firestore.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TourPlanRepository(private val firestoreService: FirestoreService) {

    // Save tourplan data to Firestore
    fun saveTourPlan(tourplan: TourPlan): Flow<Result<Unit>> = flow {
        try {
            val normalizedTourPlan = tourplan.copy(city = tourplan.normalizedCity) // Use normalized version
            firestoreService.saveTourPlan(normalizedTourPlan)
            emit(Result.success(Unit)) // Emit success when tourplan is saved
        } catch (e: Exception) {
            emit(Result.failure(e)) // Emit failure with exception if an error occurs
        }
    }

    // Get tourplan data from Firestore by ID
    fun getTourPlan(tourplanId: String): Flow<Result<Pair<TourPlan?, String?>>> = flow {
        try {
            val (tourPlan, documentId) = firestoreService.getTourPlan(tourplanId)
            Log.d("TourPlanRepository", "Fetched TourPlan with ID: $tourplanId, Result: $tourPlan, Document ID: $documentId")
            emit(Result.success(Pair(tourPlan, documentId)))
        } catch (e: Exception) {
            Log.e("TourPlanRepository", "Error fetching TourPlan for ID $tourplanId: ${e.message}", e)
            emit(Result.failure(e))
        }
    }

    // Update tourplan data in Firestore
    fun updateTourPlan(tourplan: TourPlan): Flow<Result<Unit>> = flow {
        try {
            val normalizedTourPlan = tourplan.copy(city = tourplan.normalizedCity)// Use normalized version
            firestoreService.updateTourPlan(normalizedTourPlan)
            emit(Result.success(Unit)) // Emit success when tourplan is updated
        } catch (e: Exception) {
            emit(Result.failure(e)) // Emit failure with exception if an error occurs
        }
    }

    // Get tourplans by city
    fun getTourPlansByCity(city: String): Flow<Result<List<TourPlan>>> = flow {
        try {
            // convert city to lower case and split by " "
            val tourPlans = firestoreService.getTourPlansByCity(city.trim().lowercase())
            emit(Result.success(tourPlans)) // Emit success with list of tourplans
        } catch (e: Exception) {
            emit(Result.failure(e)) // Emit failure with exception if an error occurs
        }
    }

    // Get all destinations from a tourplan
    fun getAllDestinations(tourplanId: String): Flow<Result<List<Destination>>> = flow {
        try {
            val destinations = firestoreService.getDestinationsFromTourPlan(tourplanId)
            emit(Result.success(destinations)) // Emit success with list of destinations
        } catch (e: Exception) {
            emit(Result.failure(e)) // Emit failure with exception if an error occurs
        }
    }


    // Get the number of destinations in a tourplan
    fun getDestinationsCount(tourPlanId: String): Flow<Result<Int>> = flow {
        try {
            // Call FirestoreService to fetch the count of destinations
            val count = firestoreService.getDestinationsCountByTourPlanId(tourPlanId)
            emit(Result.success(count)) // Emit success with the count of destinations
        } catch (e: Exception) {
            emit(Result.failure(e)) // Emit failure with exception if an error occurs
        }
    }


}