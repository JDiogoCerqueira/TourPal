package com.tourpal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tourpal.data.model.repository.TourPlanRatingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class TourPlanRatingViewModel(
    private val repository: TourPlanRatingRepository
) : ViewModel() {

    private val _averageRating = MutableStateFlow(0.0)
    val averageRating: StateFlow<Double> = _averageRating

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadAverageRating(tourPlanId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.getAverageRating(tourPlanId)
            result.fold(
                onSuccess = { average ->
                    _averageRating.value = average
                },
                onFailure = { exception ->
                    _errorMessage.value = exception.message
                }
            )
            _isLoading.value = false
        }
    }

}