package com.example.inoapp.database

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Solution source link:
 * https://developer.android.com/training/data-storage/room/relationships
 * One-to-Many relationship (Trip to Points)
 */

data class TripWithPoints (

    @Embedded
    val trip: Trip,

    @Relation(
        parentColumn = "tripId",        // Column of the parent entity primary key
        entityColumn = "ownerTripId"    // Child entity column that references the parent entity's primary key
    )
    val points: List<Point>
)
