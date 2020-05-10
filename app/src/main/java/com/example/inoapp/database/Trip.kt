package com.example.inoapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class Trip(

    @PrimaryKey(autoGenerate = true)
    var tripId: Long = 0L,

    @ColumnInfo(name = "title")
    var tripTitle: String = "Title",

    @ColumnInfo(name = "description")
    var tripDescription: String = "Description"

)