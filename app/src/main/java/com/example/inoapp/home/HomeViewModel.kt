package com.example.inoapp.home

import androidx.lifecycle.ViewModel
import com.example.inoapp.database.TripDatabaseDao

class HomeViewModel(
    private val tripIdKey: Long = 0L,
    dataSource: TripDatabaseDao) : ViewModel() {


}