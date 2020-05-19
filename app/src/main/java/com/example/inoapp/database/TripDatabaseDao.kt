package com.example.inoapp.database

import androidx.lifecycle.LiveData
import androidx.room.*


/**
 * Defines methods for using the Trip class with Room.
 */
@Dao
interface TripDatabaseDao {


    // TRIP WITH POINTS
    /**
     * Selecting all trips with corresponding points.
     */
    @Transaction
    @Query("SELECT * FROM trips")
    fun getTripsWithPoints(): List<TripWithPoints>

    @Transaction
    @Insert
    fun insertTripWithPoints(trip: Trip, points: List<Point>)

    // POINT
    /**
     * Inserting list of Points based on trip id.
     */
    @Insert
    fun insertPointList(points: List<Point>)

    // POINTS
    /**
     * Selects and returns the row that matches id.
     */
    @Query("SELECT * FROM points WHERE ownerTripId = :key")
    fun getPointsById(key: Long): LiveData<List<Point>>

    // TRIP
    /**
     * Inserting new trip and returning its id.
     */
    @Insert
    fun insertTrip(trip: Trip) : Long

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param trip new value to write
     */
    @Update
    fun update(trip: Trip)

    /**
     * Selects and returns the row that matches id.
     */
    @Query("SELECT * FROM trips WHERE tripId = :key")
    fun getTripById(key: Long): LiveData<Trip>

    /**
     * Deletes all values from the table.
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM trips")
    fun clear()

    /**
     * Deletes last value from the table.
     */
    @Query("DELETE FROM trips WHERE tripId=(SELECT MAX(tripId) FROM trips)")
    fun deleteLastTrip()

    /**
     * Deletes  row that matches id.
     */
    @Query("DELETE FROM trips WHERE tripId = :key")
    fun deleteTripById(key: Long)

    /**
     * Selects and returns all rows in the table,
     * sorted by id in descending order.
     */
    @Query("SELECT * FROM trips ORDER BY tripId DESC")
    fun getAllTrips(): LiveData<List<Trip>>

    /**
     * Selects and returns the latest night.
     */
    @Query("SELECT * FROM trips ORDER BY tripId DESC LIMIT 1")
    fun getLastTrip(): Trip?
}