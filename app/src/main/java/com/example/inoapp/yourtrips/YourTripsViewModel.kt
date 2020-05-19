package com.example.inoapp.yourtrips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inoapp.database.TripDatabaseDao
import kotlinx.coroutines.*

class YourTripsViewModel(dataSource: TripDatabaseDao) : ViewModel() {


    /**
     * Database and Coroutines stuff.
     */
    val database = dataSource

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * Get all trips from local database.
     */
    val trips = database.getAllTrips()

    /**
     * For navigation to trip details.
     */
    private val _navigateToTripDetail = MutableLiveData<Long>()
    val navigateToTripDetail: LiveData<Long>
        get() = _navigateToTripDetail

    /**
     * onClick() function for handling trip click event.
     */
    fun onTripClicked(id : Long) {
        _navigateToTripDetail.value = id
    }

    fun onTripDetailsNavigated() {
        _navigateToTripDetail.value = null
    }


    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.deleteLastTrip()
        }
    }

    /**
     * Executes when the CLEAR button is clicked.
     */
    fun onClear() {
        uiScope.launch {
            // Clear the database table.
            clear()
        }
    }
    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all coroutines;
     * otherwise we end up with processes that have nowhere to return to
     * using memory and resources.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}