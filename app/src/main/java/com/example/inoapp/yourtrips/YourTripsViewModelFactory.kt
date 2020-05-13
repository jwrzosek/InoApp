package com.example.inoapp.yourtrips

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.inoapp.database.TripDatabaseDao

/**
 * Provides the TripDatabaseDao and context to the ViewModel.
 */
class YourTripsViewModelFactory(
    private val dataSource: TripDatabaseDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(YourTripsViewModel::class.java)) {
            return YourTripsViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
