package com.example.inoapp.alltrips

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inoapp.database.Point
import com.example.inoapp.database.Trip
import com.example.inoapp.database.TripDatabaseDao
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

class TripListViewModel(dataSource: TripDatabaseDao) : ViewModel() {

    var firestoreDB = FirebaseFirestore.getInstance()
    /**
     * Database and Coroutines stuff.
     */
    val database = dataSource

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var points = mutableListOf<Point>()

    private val _progressBarVisible= MutableLiveData<Int>()
    val progressBarVisible: LiveData<Int>
        get() = _progressBarVisible

    private val _trips = MutableLiveData<List<Trip>>()
    val trips: LiveData<List<Trip>>
        get() = _trips

    private var tripsId = mutableListOf<String>()

    private var tripss = mutableListOf<Trip>()

    init {
        _progressBarVisible.value = View.VISIBLE
        firestoreDB.collection("trips")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    tripsId.add(document.id)
                    tripss.add(Trip(
                        tripTitle = "${document.data["tripTitle"]}",
                        tripDescription = "${document.data["tripDescription"]}",
                        tripLocalization =  "${document.data["tripLocalization"]}",
                        numberOfPoints = "${document.data["numberOfPoints"]}".toInt()
                    ))
                    Log.d("AllTripsFragment", "${document.id} => ${document.data}")
                }
                tripss.forEach { trip ->
                    Log.d("AllTripsFragment", "${trip.tripTitle}, ${trip.tripDescription}, ${trip.tripLocalization}, ${trip.numberOfPoints}")
                }
                _trips.value = tripss
                _progressBarVisible.value = View.GONE
            }
            .addOnFailureListener { exception ->
                Log.d("AllTripsFragment", "Error getting documents: ", exception)
            }

    }

    /** onClick() function for handling trip click event. */
    fun onTripClicked(id : Long) {

    }

    // Room Database stuff
    /**
     * Method for inserting a trip into database
     */
    private suspend fun insert(trip: Trip) : Long {
        var tripId = 0L
        withContext(Dispatchers.IO) {
            tripId = database.insertTrip(trip)
        }
        return tripId
    }

    private suspend fun insertPointList(points: List<Point>) {
        withContext(Dispatchers.IO) {
            database.insertPointList(points)
        }
    }

    /**
     * Executes when the add_new_trip_add_button is clicked.
     */
    fun onAddNewTrip() {
        // todo: sprawdzic czy dane się zgadzają
            uiScope.launch {

                // Create a new trip, with random title,
                // and insert it into the database.
                // todo: consider using elvis operatopr ?: eg. wrongAnswer1.value ?: ""
               /* val newTrip = Trip(
                    tripTitle = requireNotNull(tripTitle.value),
                    tripDescription = requireNotNull(tripDescription.value),
                    tripLocalization = requireNotNull(tripLocalization.value),
                    numberOfPoints = requireNotNull(_numberOfPoints.value)
                )*/

                //val tripId = insert(newTrip)

                //updateNewPointsTripId(tripId)

                insertPointList(points)

               // _navigateToHomeFragment.value = true
            }
    }

    private fun updateNewPointsTripId(tripId : Long) {
        points.forEach { point ->
            point.ownerTripId = tripId
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}