package com.tourpal.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.tourpal.data.model.Destination
import com.tourpal.data.model.TourLog
import com.tourpal.data.model.TourPlan
import com.tourpal.data.model.repository.TourPlanRepository
import com.tourpal.services.firestore.FirestoreService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class TourDetailsState(
    val tourPlan: TourPlan? = null,
    val tourPlanDocumentId: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class TourDetailsViewModel(
private val tourPlanRepository: TourPlanRepository,
private val firestoreService: FirestoreService
) : ViewModel() {

    private val _state = MutableStateFlow(TourDetailsState()).also {
        it.onEach { state ->
            Log.d("TourDetailsViewModel", "State updated: $state")
        }.launchIn(viewModelScope)
    }
    val state: StateFlow<TourDetailsState> = _state

    init {
        Log.d("TourDetailsViewModel", "TourDetailsViewModel created")
    }

    fun loadTourPlan(tourPlanId: String) {
        viewModelScope.launch {
            Log.d("TourDetailsViewModel", "Starting loadTourPlan for ID $tourPlanId")
            if (FirebaseAuth.getInstance().currentUser == null) {
                Log.w("TourDetailsViewModel", "User not authenticated")
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "User not authenticated"
                )
                return@launch
            }
            _state.value = _state.value.copy(isLoading = true, error = null)
            tourPlanRepository.getTourPlan(tourPlanId).collectLatest { result ->
                result.onSuccess { pair ->
                    val (tourPlan, documentId) = pair
                    if (tourPlan != null) {
                        Log.d("TourDetailsViewModel", "Successfully loaded TourPlan for ID $tourPlanId: $tourPlan")
                        Log.d("TourDetailsViewModel", "Setting tourPlanDocumentId to $documentId")
                        _state.value = _state.value.copy(
                            tourPlan = tourPlan,
                            tourPlanDocumentId = documentId,
                            isLoading = false
                        )
                        Log.d("TourDetailsViewModel", "Calling getAllDestinations for ID $tourPlanId")
                        getAllDestinations(tourPlanId)
                    } else {
                        Log.w("TourDetailsViewModel", "TourPlan not found for ID $tourPlanId")
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = "Tour plan not found"
                        )
                    }
                }.onFailure { exception ->
                    Log.e("TourDetailsViewModel", "Failed to load TourPlan for ID $tourPlanId: ${exception.message}", exception)
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load tour plan"
                    )
                }
            }
        }
    }

    fun getAllDestinations(tourPlanId: String) {
        viewModelScope.launch {
            Log.d("TourDetailsViewModel", "Starting getAllDestinations for tourPlanId $tourPlanId")
            if (FirebaseAuth.getInstance().currentUser == null) {
            Log.w("TourDetailsViewModel", "User not authenticated in getAllDestinations")
            _state.value = _state.value.copy(
                isLoading = false,
                error = "User not authenticated"
            )
            return@launch
            }
            try {
            val documentId = _state.value.tourPlanDocumentId ?: run {
                Log.e("TourDetailsViewModel", "Tour plan document ID not found for tourPlanId $tourPlanId")
                return@launch
            }
            Log.d("TourDetailsViewModel", "Fetching destinations for documentId $documentId")
            val destinations = firestoreService.getDestinationsFromTourPlan(documentId)
            Log.d("TourDetailsViewModel", "Fetched destinations for tourPlanId $tourPlanId (documentId $documentId): $destinations")

            _state.value = _state.value.copy(
                tourPlan = _state.value.tourPlan?.copy(
                destinations = destinations
                ),
                isLoading = false
            )
            } catch (e: Exception) {
            Log.e("TourDetailsViewModel", "Error fetching destinations: ${e.message}", e)
            _state.value = _state.value.copy(
                isLoading = false,
                error = "Failed to fetch destinations: ${e.message}"
            )
            }
        }
    }

    fun startTour(tourPlanId: String, onTourStarted: (String) -> Unit) {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val tourLogData = mapOf(
                "tourPlanId" to tourPlanId,
                "userId" to userId,
                "createdAt" to Timestamp.now(),
                "walkedPath" to emptyList<Map<String, Any>>(),
                "logs" to emptyList<Map<String, Any>>()
            )

            try {
                val tourLogId = firestoreService.firestore.collection("tour_logs").document().id
                firestoreService.firestore.collection("tour_logs").document(tourLogId).set(tourLogData).await()
                onTourStarted(tourLogId)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = "Failed to start tour: ${e.message}")
            }
        }
    }
}

class TourDetailsViewModelFactory(
    private val tourPlanRepository: TourPlanRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TourDetailsViewModel::class.java)) {
            return TourDetailsViewModel(tourPlanRepository, FirestoreService()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}