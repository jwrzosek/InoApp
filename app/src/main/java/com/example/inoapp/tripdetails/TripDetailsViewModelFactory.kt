package com.example.inoapp.tripdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.inoapp.database.TripDatabaseDao

/**
* Provides the TripDatabaseDao and tripIdKey to the ViewModel.
*/
class TripDetailsViewModelFactory(
    private val tripIdKey: Long,
    private val dataSource: TripDatabaseDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripDetailsViewModel::class.java)) {
            return TripDetailsViewModel(tripIdKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}