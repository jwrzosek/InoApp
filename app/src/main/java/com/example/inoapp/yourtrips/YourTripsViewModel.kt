package com.example.inoapp.yourtrips

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.inoapp.database.Point
import com.example.inoapp.database.Trip
import com.example.inoapp.database.TripDatabaseDao
import kotlinx.coroutines.*

class YourTripsViewModel(dataSource: TripDatabaseDao) : ViewModel() {

    val database = dataSource

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val trips = database.getAllTrips()

    //private lateinit var twp: List<TripWithPoints> todo: delete when stop being needed for testing

    init {
        //initializeTrips() // todo: delete when stop being needed for testing
    }

    /* // todo: delete when stop being needed for testing
    private fun initializeTrips() {
        uiScope.launch {
            twp = getTripWithPointsFromDatabase()
            Log.d("YourTripsViewModel",
                "${twp.last().trip.tripTitle}\n" +
            "${twp.last().points.last().ownerTripId}" +
                    "\"${twp.last().points.last().pointLatitude}" +
                    "\"${twp.last().points.last().pointQuestion}")
        }
    }
    private suspend fun getTripWithPointsFromDatabase(): List<TripWithPoints> {
        return withContext(Dispatchers.IO) {
            val tripWithPoints = database.getTripsWithPoints()
            tripWithPoints
        }
    }*/



    private suspend fun insertTrip(trip: Trip) : Long{
        var tripId = 0L
        withContext(Dispatchers.IO) {
            tripId = database.insertTrip(trip)
            Log.d("YourTripsViewModel", "id = $tripId") // todo: delete when stop being needed for testing
        }
        return tripId
    }

    private suspend fun insertPointList(points: List<Point>) {
        withContext(Dispatchers.IO) {
            database.insertPointList(points)
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.deleteLastTrip()
        }
    }

    /**
     * Executes when the Add New Trip button is clicked.
     */
    fun onStart() {
        uiScope.launch {

            // Create a new trip with data from UI
            val newTrip = Trip(tripTitle = "Random", tripDescription = "description")

            // Insert Trip data and get inserted trip id
            val tripId = insertTrip(newTrip)
            Log.d("YourTripsViewModel", "id = $tripId") // todo: delete when stop being needed for testing

            // Create list of added points and insert them into
            // database using tripId received in previous step
            val points = listOf(
                Point(
                    ownerTripId = tripId,
                    pointLatitude = 52.181510,
                    pointLongitude = 21.054533,
                    pointDescription = "desc",
                    pointQuestion = "question",
                    rightAnswer = "right",
                    wrongAnswer1 = "wrong1",
                    wrongAnswer2 = "wrong2"),
                Point(
                    ownerTripId = tripId,
                    pointLatitude = 52.181510,
                    pointLongitude = 21.054533,
                    pointDescription = "desc",
                    pointQuestion = "question",
                    rightAnswer = "right",
                    wrongAnswer1 = "wrong3",
                    wrongAnswer2 = "wrong4"))

            insertPointList(points)
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