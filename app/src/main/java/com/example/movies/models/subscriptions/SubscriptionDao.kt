package com.example.movies.models.subscriptions

import androidx.room.*

@Dao
interface SubscriptionDao {

    @Query("SELECT * FROM subscriptions")
    suspend fun getAll(): MutableList<Subscription>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subscription: Subscription)

    @Delete
    suspend fun delete(subscription: Subscription)

    @Query("DELETE FROM subscriptions WHERE showId = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM subscriptions WHERE showId = :id")
    suspend fun getById(id: Int): Subscription

}