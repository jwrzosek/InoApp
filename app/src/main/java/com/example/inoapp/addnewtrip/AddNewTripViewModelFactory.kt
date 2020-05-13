package com.example.inoapp.addnewtrip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Provides the TripDatabaseDao and context to the ViewModel.
 */
class AddNewTripViewModelFactory : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNewTripViewModel::class.java)) {
            return AddNewTripViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
