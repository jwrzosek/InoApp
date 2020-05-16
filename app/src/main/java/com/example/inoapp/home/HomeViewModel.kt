package com.example.inoapp.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inoapp.database.TripDatabaseDao

class HomeViewModel(
    private val tripIdKey: Long = 0L) : ViewModel() {

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

    init {
        Log.d("HomeViewModel", "HomeViewModel destroyed!") // todo: delete if not needed
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
        _navigateToYourTrips.value = null
        _navigateToTripList.value = null
        _navigateToAddNewTrip.value = null
        _navigateToAbout.value = null
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("HomeViewModel", "HomeViewModel destroyed!") // todo: delete if not needed
    }
}