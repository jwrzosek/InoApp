package com.example.inoapp.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.inoapp.database.TripDatabaseDao

/**
 * Provides the selectedTripId to the ViewModel.
 */
class GameViewModelFactory(
    private val selectedTripId: Long,
    private val currentPointIndex: Int,
    private val dataSource: TripDatabaseDao): ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(selectedTripId, currentPointIndex, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}