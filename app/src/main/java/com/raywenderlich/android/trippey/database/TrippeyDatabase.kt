package com.raywenderlich.android.trippey.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.raywenderlich.android.trippey.database.DatabaseConstants.DATABASE_NAME
import com.raywenderlich.android.trippey.database.DatabaseConstants.DATABASE_VERSION
import com.raywenderlich.android.trippey.database.DatabaseConstants.SQL_CREATE_ENTRIES
import com.raywenderlich.android.trippey.database.DatabaseConstants.SQL_DELETE_ENTRIES
import com.raywenderlich.android.trippey.model.Trip

/**
 * SQL database class extending SQLiteOpenHelper for the creation
 * of SQLite database
 */
class TrippeyDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Database is created here
    override fun onCreate(database: SQLiteDatabase?) {
        database?.execSQL(SQL_CREATE_ENTRIES)
    }

    // Database is migrated here
    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        database?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(database)
    }

    fun saveTrip(trip: Trip) {
        // TODO
    }

    fun updateTrip(trip: Trip) {
        // TODO
    }

    fun deleteTrip(tripId: String) {
        // TODO
    }

    fun getTrips(): List<Trip> {
        return emptyList() // TODO
    }

}