package com.tourpal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tourpal.data.model.TourPlan
import com.tourpal.data.model.repository.TourPlanRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TourPlanViewModel(private val repository: TourPlanRepository) : ViewModel() {
    private val _tourPlans = MutableStateFlow<Result<List<TourPlan>>>(Result.success(emptyList()))
    val tourPlans: StateFlow<Result<List<TourPlan>>> = _tourPlans.asStateFlow()

    fun loadTourPlansByCity(city: String) {
        viewModelScope.launch {
            repository.getTourPlansByCity(city)
                .catch { e -> _tourPlans.value = Result.failure(e) }
                .collect { result -> _tourPlans.value = result }
        }
    }
}