package com.example.movies.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shows")
data class Shows(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "poster_path") val poster_path: String?,
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "first_air_date") val first_air_date: String,
    @ColumnInfo(name = "genre") val genre: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "subscribed") var subscribed: Boolean
)