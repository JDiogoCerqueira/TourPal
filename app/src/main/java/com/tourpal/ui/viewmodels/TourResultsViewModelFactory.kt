package com.tourpal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tourpal.data.model.repository.TourPlanRatingRepository
import com.tourpal.data.model.repository.TourPlanRepository

class TourResultsViewModelFactory(
    private val tourPlanRepository: TourPlanRepository,
    private val tourPlanRatingRepository: TourPlanRatingRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TourResultsViewModel::class.java)) {
            return TourResultsViewModel(tourPlanRepository, tourPlanRatingRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}