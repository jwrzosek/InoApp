package com.example.inoapp.home

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class HomeViewModel(
    tripId: Long = 0L) : ViewModel() {

    private val _selectedTripId = MutableLiveData<Long>()
    val selectedTripId: LiveData<Long>
        get() = _selectedTripId

    private val _navigateToGame = MutableLiveData<Boolean?>()
    val navigateToGame: LiveData<Boolean?>
        get() = _navigateToGame

    private val _navigateToYourTrips = MutableLiveData<Boolean?>()
    val navigateToYourTrips: LiveData<Boolean?>
        get() = _navigateToYourTrips

    private val _navigateToTripList = MutableLiveData<Boolean?>()
    val navigateToTripList: LiveData<Boolean?>
        get() = _navigateToTripList

    private val _navigateToAddNewTrip = MutableLiveData<Boolean?>()
    val navigateToAddNewTrip: LiveData<Boolean?>
        get() = _navigateToAddNewTrip

    private val _navigateToAbout = MutableLiveData<Boolean?>()
    val navigateToAbout: LiveData<Boolean?>
        get() = _navigateToAbout

    val gameButtonVisible = Transformations.map(selectedTripId) {
        if (0L != it) View.VISIBLE
        else View.GONE
    }

    val noTripSelectedTextVisible = Transformations.map(selectedTripId) {
        if (0L != it) View.GONE
        else View.VISIBLE
    }


    init {
        _selectedTripId.value = tripId
        Log.d("HomeViewModel", "HomeViewModel destroyed!") // todo: delete if not needed
    }

    /** onClick() method for goToYourTrips */
    fun onGoToGame() {
        _navigateToGame.value = true
    }

    /** onClick() method for goToYourTripsButton */
    fun onGoToYourTrips() {
        _navigateToYourTrips.value = true
    }

    /** onClick() method for goToTripListButton */
    fun onGoToTripList() {
        _navigateToTripList.value = true
    }

    /** onClick() method for goToAddNewTripButton */
    fun onGoToAddNewTrip() {
        _navigateToAddNewTrip.value = true
    }

    /** onClick() method for goToAboutButton */
    fun onGoToAbout() {
        _navigateToAbout.value = true
    }

    /** Call this after navigation */
    fun doneNavigating() {
        _navigateToGame.value = null
        _navigateToYourTrips.value = null
        _navigateToTripList.value = null
        _navigateToAddNewTrip.value = null
        _navigateToAbout.value = null
    }

    /** Call this when onReasume is invoked  */
    fun updateTripId(tripId: Long) {
        _selectedTripId.value = tripId
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("HomeViewModel", "HomeViewModel destroyed!") // todo: delete if not needed
    }
}