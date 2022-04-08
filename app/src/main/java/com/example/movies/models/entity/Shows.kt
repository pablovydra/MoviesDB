package com.example.movies.models.entity

data class Shows(
    val id: Int,
    val poster_path: String?,
    val overview: String,
    val first_air_date: String,
    val genre_ids: List<Int>?,
    val genre: String,
    val name: String,
    val subscribed: Boolean
)
