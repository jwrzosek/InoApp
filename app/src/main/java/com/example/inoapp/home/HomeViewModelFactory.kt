package com.example.inoapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.inoapp.database.TripDatabaseDao

/**
 * Provides the TripDatabaseDao and tripIdKey to the ViewModel.
 */
class HomeViewModelFactory(private val tripIdKey: Long): ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(tripIdKey) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}