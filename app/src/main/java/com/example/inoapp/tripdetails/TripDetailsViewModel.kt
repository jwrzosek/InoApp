package com.example.inoapp.tripdetails


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inoapp.database.Trip
import com.example.inoapp.database.TripDatabaseDao
import kotlinx.coroutines.*

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

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val trip: LiveData<Trip>

    fun getTrip() = trip

    private val _navigateToYourTrips = MutableLiveData<Boolean?>()
    val navigateToYourTrips: LiveData<Boolean?>
        get() = _navigateToYourTrips

    private val _navigateToYourTripsWhenDeleted = MutableLiveData<Boolean?>()
    val navigateToYourTripsWhenDeleted: LiveData<Boolean?>
        get() = _navigateToYourTripsWhenDeleted

    private val _navigateToHomeScreen = MutableLiveData<Boolean?>()
    val navigateToHomeScreen: LiveData<Boolean?>
        get() = _navigateToHomeScreen

    init {
        trip = database.getTripById(tripIdKey)
        Log.d("TripDetailsViewModel", "TripDetailsViewModel created!")
    }

    /** Call this after navigation */
    fun doneNavigating() {
        _navigateToYourTrips.value = null
        _navigateToHomeScreen.value = null
        _navigateToYourTripsWhenDeleted.value = null
    }

    private suspend fun deleteTrip(tripId: Long) {
        withContext(Dispatchers.IO) {
            database.deleteTripById(tripId)
            database.deletePointsByTripID(tripId)
        }
    }

    /** onClick() method for Start Trip Button */
    fun onStartTrip() {
        _navigateToHomeScreen.value = true
        // When observer in TripDetailsFragment observe _navigateToHomeScreen change
        // invoke a function saveTripIdInSharedPreferences() from  TripDetailsFragment
        // which saves id of started trip in SharedPreferences for later use in
        // HomeFragment, so here we only change _navigateToHomeScreen value to true
        // note: SharedPreferences needs activity reference so this save is happening in Fragment
    }

    /** onClick() method for DeleteButton */
    fun onDeleteTrip() {
        uiScope.launch {
            // Clear the database table.
            deleteTrip(tripIdKey)
            _navigateToYourTripsWhenDeleted.value = true
        }
    }

    /** onClick() method for BackButton */
    fun onBack() {
        _navigateToYourTrips.value = true
    }


    /** Cancels all coroutines when the ViewModel is cleared, to cleanup any pending work. */
    override fun onCleared() {
        super.onCleared()
        Log.d("TripDetailsViewModel", "TripDetailsViewModel destroyed!")
        viewModelJob.cancel()
    }
}