package com.example.movies.models.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Shows::class], version = 1, exportSchema = false)
abstract class ShowsDatabase : RoomDatabase() {
    abstract fun showsDao(): ShowsDao
}