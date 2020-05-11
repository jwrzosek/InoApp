package com.example.inoapp.addnewtrip

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddNewTripViewModel : ViewModel() {

    private val _latitude = MutableLiveData<Double>()
    val latitude: LiveData<Double>
        get() = _latitude

    private val _longitude = MutableLiveData<Double>()
    val longitude: LiveData<Double?>
        get() = _longitude

    init {
        _latitude.value = 52.181510
        _longitude.value = 21.054533
    }

    fun onCoordinatesChanged(lat : Double, lng : Double) {
        _latitude.value = lat
        _longitude.value = lng
    }
}