package com.example.inoapp.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Provides the selectedTripId to the ViewModel.
 */
class GameViewModelFactory(
    private val selectedTripId: Long): ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(selectedTripId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}