package com.raywenderlich.android.trippey.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raywenderlich.android.trippey.database.DatabaseConstants.COLUMN_COUNTRY
import com.raywenderlich.android.trippey.database.DatabaseConstants.COLUMN_DETAILS
import com.raywenderlich.android.trippey.database.DatabaseConstants.COLUMN_ID
import com.raywenderlich.android.trippey.database.DatabaseConstants.COLUMN_IMAGE_URL
import com.raywenderlich.android.trippey.database.DatabaseConstants.COLUMN_LOCATIONS
import com.raywenderlich.android.trippey.database.DatabaseConstants.COLUMN_TITLE
import com.raywenderlich.android.trippey.database.DatabaseConstants.DATABASE_NAME
import com.raywenderlich.android.trippey.database.DatabaseConstants.DATABASE_VERSION
import com.raywenderlich.android.trippey.database.DatabaseConstants.QUERY_BY_ID
import com.raywenderlich.android.trippey.database.DatabaseConstants.SQL_CREATE_ENTRIES
import com.raywenderlich.android.trippey.database.DatabaseConstants.SQL_DELETE_ENTRIES
import com.raywenderlich.android.trippey.database.DatabaseConstants.SQL_UPDATE_DATABASE_ADD_LOCATIONS
import com.raywenderlich.android.trippey.database.DatabaseConstants.TRIP_TABLE_NAME
import com.raywenderlich.android.trippey.model.Trip
import com.raywenderlich.android.trippey.model.TripLocation

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
        if (oldVersion == 1 && newVersion == 2) {
            database?.execSQL(SQL_UPDATE_DATABASE_ADD_LOCATIONS)
        } else {
            database?.execSQL(SQL_DELETE_ENTRIES)
            onCreate(database)
        }

    }

    fun saveTrip(trip: Trip) {
        val database = writableDatabase ?: return

        val newValues = ContentValues().apply {
            put(COLUMN_ID, trip.id)
            put(COLUMN_TITLE, trip.title)
            put(COLUMN_COUNTRY, trip.country)
            put(COLUMN_DETAILS, trip.details)
            put(COLUMN_IMAGE_URL, trip.imageUrl)
            put(COLUMN_LOCATIONS, gson.toJson(trip.locations))
        }

        database.insert(TRIP_TABLE_NAME, null, newValues)
    }

    fun updateTrip(trip: Trip) {
        val database = writableDatabase ?: return

        val selection = QUERY_BY_ID
        val selectionArguments = arrayOf(trip.id)

        val newValues = ContentValues().apply {
            put(COLUMN_LOCATIONS, gson.toJson(trip.locations))
        }

        database.update(TRIP_TABLE_NAME, newValues, selection, selectionArguments)
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
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DETAILS)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)),
                parseTripLocationFromJson(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATIONS)))
            )
            )
        }

        cursor.close()

        return items
    }

    private fun parseTripLocationFromJson(json: String?): List<TripLocation> {
        if (json == null) return emptyList()

        val typeToken = object : TypeToken<List<TripLocation>>() {}.type

        return try {
            gson.fromJson(json, typeToken)
        }catch (error: Throwable) {
            error.printStackTrace()
            emptyList()
        }
    }

}