package com.example.movies.models.repository

import com.example.movies.models.dto.ApiResource
import com.example.movies.models.network.DiscoverTvResponse
import com.example.movies.models.services.MoviesApiService
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(private val moviesApiService: MoviesApiService) :
    BaseRepository(), MoviesRepository {

    override suspend fun getDiscoverTv(page: Int): ApiResource<DiscoverTvResponse> {
        return getResult { moviesApiService.getDiscoverTv(page) }
    }

}