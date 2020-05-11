package com.example.inoapp.yourtrips

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inoapp.database.Trip
import com.example.inoapp.database.TripDatabaseDao
import kotlinx.coroutines.*

class YourTripsViewModel(dataSource: TripDatabaseDao, application: Application) : ViewModel() {

    val database = dataSource

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var lastTrip = MutableLiveData<Trip?>()

    val trips = database.getAllTrips()

    init {
        initializeTrips()
    }

    private fun initializeTrips() {
        uiScope.launch {
            lastTrip.value = getTonightFromDatabase()
        }
    }

    /**
     *  Handling the case of the stopped app or forgotten recording,
     *  the start and end times will be the same.j
     *
     *  If the start time and end time are not the same, then we do not have an unfinished
     *  recording.
     */
    private suspend fun getTonightFromDatabase(): Trip? {
        return withContext(Dispatchers.IO) {
            val lastTrip = database.getLastTrip()
            lastTrip
        }
    }

    private suspend fun insert(trip: Trip) {
        withContext(Dispatchers.IO) {
            database.insert(trip)
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.deleteLastTrip()
        }
    }

    /**
     * Executes when the START button is clicked.
     */
    fun onStart() {
        uiScope.launch {
            // Create a new trip, with random title,
            // and insert it into the database.
            val newTrip = Trip(tripTitle = "Random title")

            insert(newTrip)
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