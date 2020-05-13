package com.example.inoapp.addnewtrip

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inoapp.database.Trip
import com.example.inoapp.database.TripDatabaseDao
import kotlinx.coroutines.*

open class AddNewTripViewModel(dataSource: TripDatabaseDao) : ViewModel() {

    // Room Database and Coroutines stuff

    val database = dataSource
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * Variable that tells the fragment whether it should navigate to HomeFragment.
     */
    private val _navigateToHomeFragment = MutableLiveData<Boolean?>()
    /**
     * When true immediately navigate back to the HomeFragment
     */
    val navigateToHomeFragment: LiveData<Boolean?>
        get() = _navigateToHomeFragment


    /**
     *  Point description attributes.
     */

    // MapFragment

    private val _latitude = MutableLiveData<Double>()
    val latitude: LiveData<Double>
        get() = _latitude

    private val _longitude = MutableLiveData<Double>()
    val longitude: LiveData<Double>
        get() = _longitude

    // AddNewPointFragment

    /**
     * For two-way data binding exposing properties as MutableLiveData is needed :(
     * So this is the reason that edit text properties as exposed like this
     */
    val pointDescription = MutableLiveData<String>()

    val pointQuestion = MutableLiveData<String>()

    val rightAnswer = MutableLiveData<String>()

    val wrongAnswer1 = MutableLiveData<String>()

    val wrongAnswer2 = MutableLiveData<String>()


    /**
     *  Trip description attributes (AddNewTripFragment).
     */

    val tripTitle = MutableLiveData<String>()

    val tripDescription = MutableLiveData<String>()

    /**
     *  Initialization.
     */
    init {
        _latitude.value = 52.181510
        _longitude.value = 21.054533
        Log.d("AddNewTripViewModel", "AddNewTripViewModel was created!")
    }


    fun onCoordinatesChanged(lat: Double, lng: Double) {
        _latitude.value = lat
        _longitude.value = lng
    }

    // Room Database stuff
    private suspend fun insert(trip: Trip) {
        withContext(Dispatchers.IO) {
            database.insert(trip)
        }
    }

    /**
     * Executes when the START button is clicked.
     */
    fun onAddNewTrip() {
        uiScope.launch {
            // Create a new trip, with random title,
            // and insert it into the database.
            val newTrip = Trip(tripTitle = "From AddNewTripFragment")

            insert(newTrip)

            _navigateToHomeFragment.value = true
        }
    }

    fun doneNavigating() {
        _navigateToHomeFragment.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        Log.d("AddNewTripViewModel", "AddNewTripViewModel was destroyed!")
    }
}