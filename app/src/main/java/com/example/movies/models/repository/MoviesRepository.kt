package com.example.movies.models.repository

import com.example.movies.models.dto.ApiResource
import com.example.movies.models.network.DiscoverTvResponse
import com.example.movies.models.network.GenresTvResponse

interface MoviesRepository {
    suspend fun getDiscoverTv(page: Int): ApiResource<DiscoverTvResponse>
    suspend fun getGenresTv(): ApiResource<GenresTvResponse>
}