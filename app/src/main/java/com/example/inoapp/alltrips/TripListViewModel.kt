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

    private val _navigateToHomeFragment = MutableLiveData<Boolean?>()
    val navigateToHomeFragment: LiveData<Boolean?>
        get() = _navigateToHomeFragment

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
                    Log.d("TripListViewModel", "${document.id} => ${document.data}")
                }
                tripss.forEach { trip ->
                    Log.d("TripListViewModel", "${trip.tripTitle}, ${trip.tripDescription}, ${trip.tripLocalization}, ${trip.numberOfPoints}")
                }
                _trips.value = tripss
                _progressBarVisible.value = View.GONE
            }
            .addOnFailureListener { exception ->
                Log.d("TripListViewModel", "Error getting documents: ", exception)
            }
    }

    /** onClick() function for handling trip click event. */
    fun onTripClicked(title : String) {
        _trips.value?.forEachIndexed { index, trip ->
            if (trip.tripTitle == title) {
                addNewTrip(trip, index)
            }
        }
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

    private fun addNewTrip(newTrip: Trip, index: Int) {
        // todo: sprawdzic czy dane się zgadzają
        uiScope.launch {

            val tripId = insert(newTrip)

            firestoreDB.collection("trips").document(tripsId[index]).collection("points")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        points.add(Point(
                            pointLatitude = "${document.data["pointLatitude"]}".toDouble(),
                            pointLongitude = "${document.data["pointLongitude"]}".toDouble(),
                            pointDescription = "${document.data["pointDescription"]}",
                            pointQuestion = "${document.data["pointQuestion"]}",
                            rightAnswer = "${document.data["rightAnswer"]}",
                            wrongAnswer1 = "${document.data["wrongAnswer1"]}",
                            wrongAnswer2 = "${document.data["wrongAnswer2"]}"
                        ))
                        Log.d("TripListViewModel", "${document.id} => ${document.data}")
                    }
                    tripss.forEach { trip ->
                        Log.d("TripListViewModel", "${trip.tripTitle}, ${trip.tripDescription}, ${trip.tripLocalization}, ${trip.numberOfPoints}")
                    }
                    updateNewPointsTripId(tripId)
                    _progressBarVisible.value = View.GONE
                }
                .addOnFailureListener { exception ->
                    Log.d("TripListViewModel", "Error getting documents: ", exception)
                }
        }
    }

    private fun updateNewPointsTripId(tripId : Long) {
        points.forEach { point ->
            point.ownerTripId = tripId
        }
        uiScope.launch {
            insertPointList(points)
            _navigateToHomeFragment.value = true
        }
    }

    fun doneNavigating() {
        _navigateToHomeFragment.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}