package com.example.inoapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing database entity for a Trip.
 */
@Entity(tableName = "points")
data class Point(

    @PrimaryKey(autoGenerate = true)
    val pointId: Long = 0L,

    @ColumnInfo(name = "ownerTripId")
    val ownerTripId: Long,

    @ColumnInfo(name = "latitude")
    val pointLatitude: Double,

    @ColumnInfo(name = "longitude")
    val pointLongitude: Double,

    @ColumnInfo(name = "description")
    val pointDescription: String,

    @ColumnInfo(name = "question")
    val pointQuestion: String,

    @ColumnInfo(name = "rightAnswer")
    val rightAnswer: String,

    @ColumnInfo(name = "wrongAnswer1")
    val wrongAnswer1: String,

    @ColumnInfo(name = "wrongAnswer2")
    val wrongAnswer2: String

)