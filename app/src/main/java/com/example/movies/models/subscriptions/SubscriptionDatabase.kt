package com.example.movies.models.subscriptions

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Subscription::class], version = 1, exportSchema = false)
abstract class SubscriptionDatabase : RoomDatabase() {
    abstract fun subscriptionDao(): SubscriptionDao
}