package com.example.movies.models.repository

import com.example.movies.models.dto.ApiResource
import com.example.movies.models.network.DiscoverTvResponse

interface MoviesRepository {
    suspend fun getDiscoverTv(page: Int): ApiResource<DiscoverTvResponse>
}