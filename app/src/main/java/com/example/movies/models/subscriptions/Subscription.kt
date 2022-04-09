package com.example.movies.models.subscriptions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscriptions")
data class Subscription(
    @ColumnInfo(name = "showId") val showId: Int,
    @ColumnInfo(name = "poster_path") val poster_path: String?,
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "first_air_date") val first_air_date: String,
    @ColumnInfo(name = "genre") val genre: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "subscribed") val subscribed: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}

