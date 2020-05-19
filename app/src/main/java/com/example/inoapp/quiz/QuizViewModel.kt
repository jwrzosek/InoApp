package com.example.inoapp.quiz

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inoapp.database.Point
import com.example.inoapp.database.Trip
import com.example.inoapp.database.TripDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class QuizViewModel(
    private val tripId: Long = 0L,
    private val currentIndex: Int = 0,
    dataSource: TripDatabaseDao) : ViewModel() {

    /** Hold a reference to InoDatabase via its TripDatabaseDao. */
    val database = dataSource

    /** Coroutine setup variables */
    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()

    private val points: LiveData<List<Point>>
    fun getPoints() = points

    private val _navigateBackToGame = MutableLiveData<Boolean?>()
    val navigateBackToGame: LiveData<Boolean?>
        get() = _navigateBackToGame

    private val _showRightAnswerToast = MutableLiveData<Boolean?>()
    val showRightAnswerToast: LiveData<Boolean?>
        get() = _showRightAnswerToast

    private val _showWrongAnswerToast = MutableLiveData<Boolean?>()
    val showWrongAnswerToast: LiveData<Boolean?>
        get() = _showWrongAnswerToast

    private val _currentPointIndex = MutableLiveData<Int>()
    val currentPointIndex: LiveData<Int>
        get() = _currentPointIndex

    private val _quizAnswer1 = MutableLiveData<Int>()
    val quizAnswer1: LiveData<Int>
        get() = _quizAnswer1

    private val _quizAnswer2 = MutableLiveData<Int>()
    val quizAnswer2: LiveData<Int>
        get() = _quizAnswer2

    private val _quizAnswer3 = MutableLiveData<Int>()
    val quizAnswer3: LiveData<Int>
        get() = _quizAnswer3

    private val _backButtonVisible = MutableLiveData<Int>()
    val backButtonVisible: LiveData<Int>
        get() = _backButtonVisible

    private val randomList = (0..2).shuffled().take(3)

    init {
        points = database.getPointsById(tripId)
        _quizAnswer1.value = randomList[0]
        _quizAnswer2.value = randomList[1]
        _quizAnswer3.value = randomList[2]
        _currentPointIndex.value = currentIndex
        _backButtonVisible.value = View.GONE
        Log.d("QuizViewModel", "QuizViewModel created!") // todo: delete if not needed
    }


    /** onClick() for game_start_quiz_button */
    fun onAnswer1() {
        if (randomList[0] == 0) {
            _showRightAnswerToast.value = true
            _backButtonVisible.value = View.VISIBLE
        }
        else {
            _showWrongAnswerToast.value = true
        }
    }
    /** onClick() for game_start_quiz_button */
    fun onAnswer2() {
        if (randomList[1] == 0) {
            _showRightAnswerToast.value = true
            _backButtonVisible.value = View.VISIBLE
        }
        else {
            _showWrongAnswerToast.value = true
        }
    }
    /** onClick() for game_start_quiz_button */
    fun onAnswer3() {
        if (randomList[2] == 0) {
            _showRightAnswerToast.value = true
            _backButtonVisible.value = View.VISIBLE
        }
        else {
            _showWrongAnswerToast.value = true
        }
    }

    /** onClick() for backToGame button*/
    fun onBack() {
        _navigateBackToGame.value = true
    }

    /** Call after navigating */
    fun doneNavigating() {
        _navigateBackToGame.value = null
    }

    /** Call after showing Toast*/
    fun doneShowingToast() {
        _showRightAnswerToast.value = null
        _showWrongAnswerToast.value = null
    }

    /** Cancels all coroutines when the ViewModel is cleared, to cleanup any pending work. */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        Log.d("QuizViewModel", "QuizViewModel destroyed!") // todo: delete if not needed
    }
}