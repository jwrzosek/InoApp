package com.example.inoapp.game

import android.location.Location
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.inoapp.database.Point
import com.example.inoapp.database.Trip
import com.example.inoapp.database.TripDatabaseDao
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class GameViewModel(
    tripId: Long = 0L,
    currentIndex: Int = 0,
    dataSource: TripDatabaseDao) : ViewModel() {

    private val MINIMUM_DISTANCE_FOR_QUIZ: Float = 100F

    /** Hold a reference to InoDatabase via its TripDatabaseDao. */
    val database = dataSource

    /** Coroutine setup variables */
    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()

    private val trip: LiveData<Trip>
        fun getTrip() = trip

    private val points: LiveData<List<Point>>
        fun getPoints() = points

    private val _distanceToNextPoint = MutableLiveData<Float>()
    val distanceToNextPoint: LiveData<Float>
        get() = _distanceToNextPoint

    private val _currentPointIndex = MutableLiveData<Int>()
    val currentPointIndex: LiveData<Int>
        get() = _currentPointIndex

    private val _navigateToQuiz = MutableLiveData<Boolean?>()
    val navigateToQuiz: LiveData<Boolean?>
        get() = _navigateToQuiz

    private val _endTrip = MutableLiveData<Boolean?>()
    val endTrip: LiveData<Boolean?>
        get() = _endTrip

    private val _infoVisible = MutableLiveData<Int>()
    val infoVisible: LiveData<Int>
        get() = _infoVisible

    private val _startQuizButtonVisible = MutableLiveData<Int>()
    val startQuizButtonVisible: LiveData<Int>
        get() = _startQuizButtonVisible

    private val _progressBarVisible= MutableLiveData<Int>()
    val progressBarVisible: LiveData<Int>
        get() = _progressBarVisible

    init {
        _progressBarVisible.value = View.VISIBLE
        trip = database.getTripById(tripId)
        points = database.getPointsById(tripId)
        _currentPointIndex.value = currentIndex
        _infoVisible.value = View.VISIBLE
        _startQuizButtonVisible.value = View.GONE
        Log.d("GameViewModel", "GameViewModel created!") // todo: delete if not needed
    }


    /** onClick() method for ToggleViewButton */
    fun onToggleView() {
        Log.d("GameViewModel", "onToggleView clicked") // todo: delete if not needed
        _infoVisible.value = if (_infoVisible.value == View.VISIBLE) View.GONE
                             else View.VISIBLE
    }

    /**
     * Method for updating distance left to point.
     * If distance is lesser than MINIMUM_DISTANCE_FOR_QUIZ
     * start navigation to QuizFragment
     */
    fun updateDistanceToNextPoint(location: Location) {
        val currentLocation = Location("currentLocation")
        currentLocation.latitude = requireNotNull(points.value?.get(requireNotNull(currentPointIndex.value))?.pointLatitude)
        currentLocation.longitude = requireNotNull(points.value?.get(requireNotNull(currentPointIndex.value))?.pointLongitude)

        val distanceToDestination = currentLocation.distanceTo(location)
        _distanceToNextPoint.value = distanceToDestination

        // if distancetonextpoint < 100m then navigate to quiz
        if (distanceToDestination < MINIMUM_DISTANCE_FOR_QUIZ) {
            _startQuizButtonVisible.value = View.VISIBLE
        }
    }

    /** Call this after map is ready */
    fun doneShowingProgressBar() {
        _progressBarVisible.value = View.GONE
    }

    /** Call this after navigation */
    fun doneNavigating() {
        _navigateToQuiz.value = null
        _startQuizButtonVisible.value = View.GONE
    }

    /** onClick() for game_start_quiz_button */
    fun onStartQuiz() {
        _navigateToQuiz.value = true
    }

    /** onClick() for game_end_trip_button */
    fun onEndTrip() {
        _endTrip.value = true
    }

    fun updateCurrentPointIndex(nextPointIndex: Int) {
        _currentPointIndex.value = nextPointIndex
    }

    /** Cancels all coroutines when the ViewModel is cleared, to cleanup any pending work. */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        Log.d("GameViewModel", "HomeViewModel destroyed!") // todo: delete if not needed
    }
}