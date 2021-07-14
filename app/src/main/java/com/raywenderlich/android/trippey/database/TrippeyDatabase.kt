package com.raywenderlich.android.trippey.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.Gson
import com.raywenderlich.android.trippey.database.DatabaseConstants.DATABASE_NAME
import com.raywenderlich.android.trippey.database.DatabaseConstants.DATABASE_VERSION
import com.raywenderlich.android.trippey.database.DatabaseConstants.QUERY_BY_ID
import com.raywenderlich.android.trippey.database.DatabaseConstants.SQL_CREATE_ENTRIES
import com.raywenderlich.android.trippey.database.DatabaseConstants.SQL_DELETE_ENTRIES
import com.raywenderlich.android.trippey.database.DatabaseConstants.TRIP_TABLE_NAME
import com.raywenderlich.android.trippey.model.Trip

/**
 * SQL database class extending SQLiteOpenHelper for the creation
 * of SQLite database
 */
class TrippeyDatabase(
    context: Context,
    private val gson: Gson
) :
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
        val database = writableDatabase ?: return

        val newValues = ContentValues().apply {
            put(DatabaseConstants.COLUMN_ID, trip.id)
            put(DatabaseConstants.COLUMN_TITLE, trip.title)
            put(DatabaseConstants.COLUMN_COUNTRY, trip.country)
            put(DatabaseConstants.COLUMN_DETAILS, trip.details)
            put(DatabaseConstants.COLUMN_IMAGE_URL, trip.imageUrl)

        }

        database.insert(TRIP_TABLE_NAME, null, newValues)
    }

    fun updateTrip(trip: Trip) {
        // TODO
    }

    fun deleteTrip(tripId: String) {
        val database = writableDatabase ?: return

        val selection = QUERY_BY_ID
        val selectionArguments = arrayOf(tripId)

        database.delete(TRIP_TABLE_NAME, selection, selectionArguments)
    }

    fun getTrips(): List<Trip> {
        val items = mutableListOf<Trip>()
        val database = readableDatabase ?: return items

        val cursor = database.query(TRIP_TABLE_NAME,
        null,
        null,
        null,
        null,
        null,
        null)

        while (cursor.moveToNext()) {
            items.add(
                Trip(
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.COLUMN_COUNTRY)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.COLUMN_DETAILS)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.COLUMN_IMAGE_URL))
            )
            )
        }

        cursor.close()

        return items
    }

}