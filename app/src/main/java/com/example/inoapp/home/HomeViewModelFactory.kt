package com.example.inoapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.inoapp.database.TripDatabaseDao

/**
 * Provides the selectedTripId to the ViewModel.
 */
class HomeViewModelFactory(private val selectedTripId: Long): ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(selectedTripId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}