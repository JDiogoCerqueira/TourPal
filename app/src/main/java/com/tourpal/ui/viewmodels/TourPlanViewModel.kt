package com.tourpal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tourpal.data.model.TourPlan
import com.tourpal.data.model.repository.TourPlanRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TourPlanViewModel (
    private val tourPlanRepository: TourPlanRepository
) : ViewModel() {

    // Define state variables
    private val _tourPlanState = MutableStateFlow<Result<TourPlan?>>(Result.success(null))
    val tourPlanState: StateFlow<Result<TourPlan?>> = _tourPlanState

    private val _tourPlansState = MutableStateFlow<Result<List<TourPlan>>>(Result.success(emptyList()))
    val tourPlansState: StateFlow<Result<List<TourPlan>>> = _tourPlansState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Function to load a tour plan by ID
    fun loadTourPlan(tourPlanId: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            tourPlanRepository.getTourPlan(tourPlanId)
                .onEach { result ->
                    result.fold(
                        onSuccess = { tourPlan ->
                            _tourPlanState.value = Result.success(tourPlan)
                        },
                        onFailure = { exception ->
                            _tourPlanState.value = Result.failure(exception)
                            _errorMessage.value = exception.localizedMessage
                        }
                    )
                }.collect()
            _isLoading.value = false
        }
    }

    // Function to load tour plans by city
    fun loadTourPlansByCity(city: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            tourPlanRepository.getTourPlansByCity(city)
                .onEach { result ->
                    result.fold(
                        onSuccess = { tourPlans ->
                            _tourPlansState.value = Result.success(tourPlans)
                        },
                        onFailure = { exception ->
                            _tourPlansState.value = Result.failure(exception)
                            _errorMessage.value = exception.localizedMessage
                        }
                    )
                }.collect()
            _isLoading.value = false
        }
    }


    // Function to save a new tour plan
    fun saveTourPlan(tourPlan: TourPlan) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            tourPlanRepository.saveTourPlan(tourPlan)
                .onEach { result ->
                    result.fold(
                        onSuccess = {
                            _errorMessage.value = "Tour plan saved successfully"
                        },
                        onFailure = { exception ->
                            _errorMessage.value = exception.localizedMessage
                        }
                    )
                }.collect()
            _isLoading.value = false
        }
    }

    // Function to update an existing tour plan
    fun updateTourPlan(tourPlan: TourPlan) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            tourPlanRepository.updateTourPlan(tourPlan)
                .onEach { result ->
                    result.fold(
                        onSuccess = {
                            _errorMessage.value = "Tour plan updated successfully"
                        },
                        onFailure = { exception ->
                            _errorMessage.value = exception.localizedMessage
                        }
                    )
                }.collect()
            _isLoading.value = false
        }
    }

}

