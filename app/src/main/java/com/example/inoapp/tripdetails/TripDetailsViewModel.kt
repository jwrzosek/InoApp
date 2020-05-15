package com.example.inoapp.tripdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inoapp.database.Trip
import com.example.inoapp.database.TripDatabaseDao
import kotlinx.coroutines.Job

class TripDetailsViewModel(
    private val tripIdKey: Long = 0L,
    dataSource: TripDatabaseDao) : ViewModel() {

    /**
     * Hold a reference to InoDatabase via its TripDatabaseDao.
     */
    val database = dataSource

    /** Coroutine setup variables */
    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()

    private val trip: LiveData<Trip>

    fun getTrip() = trip

    private val _navigateToYourTrips = MutableLiveData<Boolean?>()
    val navigateToYourTrips: LiveData<Boolean?>
        get() = _navigateToYourTrips

    init {
        trip = database.getTripById(tripIdKey)
    }

    /** Call this after navigation */
    fun doneNavigating() {
        _navigateToYourTrips.value = null
    }

    /** onClick() method for BackButton */
    fun onDelete() {
        _navigateToYourTrips.value = true
        // todo: remove trip logic
    }

    /** onClick() method for BackButton */
    fun onClose() {
        _navigateToYourTrips.value = true
    }


    /** Cancels all coroutines when the ViewModel is cleared, to cleanup any pending work. */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}