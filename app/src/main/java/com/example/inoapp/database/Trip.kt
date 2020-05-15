package com.example.inoapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing database entity for a Trip.
 */
@Entity(tableName = "trips")
data class Trip(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tripId")
    val tripId: Long = 0L,

    @ColumnInfo(name = "title")
    val tripTitle: String,

    @ColumnInfo(name = "description")
    val tripDescription: String,

    @ColumnInfo(name = "tripLocalization")
    val tripLocalization: String,

    @ColumnInfo(name = "numberOfPoints")
    val numberOfPoints: Int

)