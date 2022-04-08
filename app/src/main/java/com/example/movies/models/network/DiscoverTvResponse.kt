package com.example.movies.models.network

import com.example.movies.models.entity.Tv

data class DiscoverTvResponse(
    val page: Int,
    val results: List<Tv>,
    val total_results: Int,
    val total_pages: Int
)
