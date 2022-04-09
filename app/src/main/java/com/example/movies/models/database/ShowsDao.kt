package com.example.movies.models.database

import androidx.room.*

@Dao
interface ShowsDao {

    @Query("SELECT * FROM shows")
    suspend fun getAll(): List<Shows>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shows: Shows)

    @Delete
    suspend fun delete(shows: Shows)

    @Query("DELETE FROM shows WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM shows WHERE id = :id")
    suspend fun getById(id: Int): Shows

}