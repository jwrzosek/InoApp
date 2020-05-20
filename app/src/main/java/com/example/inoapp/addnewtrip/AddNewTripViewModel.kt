package com.example.inoapp.addnewtrip

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inoapp.database.Point
import com.example.inoapp.database.Trip
import com.example.inoapp.database.TripDatabaseDao
import kotlinx.coroutines.*

// todo: remind why is open class here (probably it's sth with nested graph scope viewModel)
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

    private val _navigateToAddNewTripFragment = MutableLiveData<Boolean?>()
    val navigateToAddNewTripFragment: LiveData<Boolean?>
        get() = _navigateToAddNewTripFragment

    private var _showIncompleteDataToast = MutableLiveData<Boolean>()
    val showIncompleteDataToast: LiveData<Boolean>
        get() = _showIncompleteDataToast

    private var _showNoPointsStatement = MutableLiveData<Boolean>()
    val showNoPointsStatement: LiveData<Boolean>
        get() = _showNoPointsStatement

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

    private var points = mutableListOf<Point>()



    /**
     *  Trip description attributes (AddNewTripFragment).
     */

    val tripTitle = MutableLiveData<String>()

    val tripDescription = MutableLiveData<String>()

    val tripLocalization = MutableLiveData<String>()

    private val _numberOfPoints = MutableLiveData<Int>()
    val numberOfPoints: LiveData<Int>
        get() = _numberOfPoints

    /**
     *  Initialization.
     */
    init {
        _numberOfPoints.value = 0
        tripTitle.value = ""
        tripDescription.value = ""
        tripLocalization.value = ""
        addPointCleanUp()
        Log.d("AddNewTripViewModel", "AddNewTripViewModel was created!") // todo: delete when stop being needed for testing
    }


    fun onCoordinatesChanged(lat: Double, lng: Double) {
        _latitude.value = lat
        _longitude.value = lng
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
        if(tripTitle.value != "" && tripDescription.value != ""
            && tripLocalization.value != "") {
            uiScope.launch {

                // Create a new trip, with random title,
                // and insert it into the database.
                // todo: consider using elvis operatopr ?: eg. wrongAnswer1.value ?: ""
                val newTrip = Trip(
                    tripTitle = requireNotNull(tripTitle.value),
                    tripDescription = requireNotNull(tripDescription.value),
                    tripLocalization = requireNotNull(tripLocalization.value),
                    numberOfPoints = requireNotNull(_numberOfPoints.value)
                )

                val tripId = insert(newTrip)

                updateNewPointsTripId(tripId)

                insertPointList(points)

                _navigateToHomeFragment.value = true
            }
        }
        else {
            _showIncompleteDataToast.value = true
            Log.d("AddNewTripViewModel", "You don't type trip information") // todo; changed into Toast or sth
        }
    }

    /**
     * Executes when the add_new_point_add_button is clicked.
     */
    fun onAddNewPoint() {
        if(pointQuestion.value != "" && rightAnswer.value != ""
            && wrongAnswer1.value != "" && wrongAnswer2.value != "") {

            val newPoint = Point(
                pointLatitude = requireNotNull(_latitude.value),
                pointLongitude = requireNotNull(_longitude.value),
                pointDescription = requireNotNull(pointDescription.value),
                pointQuestion = requireNotNull(pointQuestion.value),
                rightAnswer = requireNotNull(rightAnswer.value),
                wrongAnswer1 = requireNotNull(wrongAnswer1.value),
                wrongAnswer2 = requireNotNull(wrongAnswer2.value)
            )

            points.add(newPoint)
            Log.d("AddNewTripViewModel", "${pointDescription.value}") // todo; delete if not needed
            addPointCleanUp()
            increaseNumberOfPoints()
            _navigateToAddNewTripFragment.value = true
        }
        else {
            _showIncompleteDataToast.value = true
            Log.d("AddNewTripViewModel", "You don't type trip information") // todo; changed into Toast or sth
        }
    }

    /**
     * Increases LiveData holding number of points added to show it in UI
     */
    private fun increaseNumberOfPoints() {
        _numberOfPoints.value = (_numberOfPoints.value)?.plus(1)
    }

    /**
     * Decrease LiveData holding number of points added to show it in UI
     * todo: implement deleting points
     */
    private fun deacreaseNumberOfPoints() {
        if (points.size > 0) {
            points.removeAt(points.lastIndex)
            _numberOfPoints.value = (_numberOfPoints.value)?.minus(1)
        }
    }

    fun onDeleteLastPoint() {
        if (points.size > 0) {
            points.removeAt(points.lastIndex)
            _numberOfPoints.value = (_numberOfPoints.value)?.minus(1)
        } else {
            _showNoPointsStatement.value = true
        }
    }

    private fun updateNewPointsTripId(tripId : Long) {
        points.forEach { point ->
            point.ownerTripId = tripId
        }
    }

    /**
     * Executes when point was added to point list.
     * Navigate to AddNewTripFragment and clean up values of point that was added
     * to clean UI for adding another point.
     */
    private fun addPointCleanUp() {
        _latitude.value = 52.232408
        _longitude.value = 21.005845
        pointDescription.value = ""
        pointQuestion.value = ""
        rightAnswer.value = ""
        wrongAnswer1.value = ""
        wrongAnswer2.value = ""
    }

    fun doneNavigating() {
        _navigateToHomeFragment.value = null
        _navigateToAddNewTripFragment.value = null
    }

    fun doneShowingToast() {
        _showIncompleteDataToast.value = false
        _showNoPointsStatement.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        Log.d("AddNewTripViewModel", "AddNewTripViewModel was destroyed!") // todo: delete when stop being needed for testing
    }
}