package com.tourpal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tourpal.data.model.TourPlan
import com.tourpal.data.model.repository.TourPlanRatingRepository
import com.tourpal.data.model.repository.TourPlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class TourResultState(
    val tourPlans: List<TourPlan> = emptyList(),
    val ratings: Map<String, Double> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class TourResultsViewModel(
    private val tourPlanRepository: TourPlanRepository,
    private val tourPlanRatingRepository: TourPlanRatingRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TourResultState())
    val state: StateFlow<TourResultState> = _state

    fun fetchTourPlansByCity(city: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            tourPlanRepository.getTourPlansByCity(city).collectLatest { result ->
                result.onSuccess { tourPlans ->
                    _state.value = _state.value.copy(tourPlans = tourPlans, isLoading = false)

                    // Fetch ratings for each tour plan
                    val ratingsMap = mutableMapOf<String, Double>()
                    tourPlans.forEach { tourPlan ->
                        tourPlanRatingRepository.getAverageRating(tourPlan.id).let { ratingResult ->
                            ratingResult.onSuccess { avgRating ->
                                ratingsMap[tourPlan.id] = avgRating
                            }.onFailure { exception ->
                                // TODO
                            }
                        }
                    }
                    _state.value = _state.value.copy(ratings = ratingsMap)
                }.onFailure { exception ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to fetch tour plans"
                    )
                }
            }
        }
    }


}