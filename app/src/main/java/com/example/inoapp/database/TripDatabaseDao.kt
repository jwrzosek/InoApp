package com.example.inoapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


/**
 * Defines methods for using the Trip class with Room.
 */
@Dao
interface TripDatabaseDao {

    @Insert
    fun insert(trip: Trip)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param trip new value to write
     */
    @Update
    fun update(trip: Trip)

    /**
     * Selects and returns the row that matches the supplied start time, which is our key.
     *
     * @param key startTimeMilli to match
     */
    @Query("SELECT * from trips WHERE tripId = :key")
    fun get(key: Long): Trip?

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM trips")
    fun clear()

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM trips WHERE tripId=(SELECT MAX(tripId) FROM trips)")
    fun deleteLastTrip()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM trips ORDER BY tripId DESC")
    fun getAllTrips(): LiveData<List<Trip>>

    /**
     * Selects and returns the latest night.
     */
    @Query("SELECT * FROM trips ORDER BY tripId DESC LIMIT 1")
    fun getLastTrip(): Trip?
}