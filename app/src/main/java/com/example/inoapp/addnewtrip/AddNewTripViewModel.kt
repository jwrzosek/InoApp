package com.example.inoapp.addnewtrip

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inoapp.database.TripDatabaseDao

open class AddNewTripViewModel(dataSource: TripDatabaseDao) : ViewModel() {

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

    override fun onCleared() {
        super.onCleared()
        Log.d("AddNewTripViewModel", "AddNewTripViewModel was destroyed!")
    }
}