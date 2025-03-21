package com.tourpal.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit

class SearchViewModel : ViewModel() {
    private val _recentSearches = mutableStateListOf<String>()
    val recentSearches: List<String> get() = _recentSearches

    // Load recent searches from SharedPreferences
    fun loadRecentSearches(context: Context) {
        val sharedPreferences = context.getSharedPreferences("TourPalPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("recent_searches", null)
        val type = object : TypeToken<MutableList<String>>() {}.type
        val loadedSearches = if (json != null) {
            gson.fromJson<MutableList<String>>(json, type) ?: mutableListOf()
        } else {
            mutableListOf()
        }
        _recentSearches.clear()
        _recentSearches.addAll(loadedSearches)
    }

    // Save recent searches to SharedPreferences
    private fun saveRecentSearches(context: Context) {
        val sharedPreferences = context.getSharedPreferences("TourPalPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(_recentSearches)
        sharedPreferences.edit() { putString("recent_searches", json) }
    }

    // Add a new search term to the list
    fun addRecentSearch(context: Context, query: String) {
        if (query.isBlank()) return // Ignore empty searches
        // Remove the query if it already exists to avoid duplicates
        _recentSearches.remove(query)
        // Add the query to the start of the list
        _recentSearches.add(0, query)
        // Limit the list to 5 items
        if (_recentSearches.size > 5) {
            _recentSearches.removeAt(_recentSearches.size - 1)
        }
        // Save to SharedPreferences
        saveRecentSearches(context)
    }

    // Clear a specific search term from the list
    fun clearSearch(context: Context, query: String) {
        _recentSearches.remove(query)
        saveRecentSearches(context)
    }

}