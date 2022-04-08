package com.example.movies.models.usecase

import com.example.movies.models.dto.ApiResource
import com.example.movies.models.network.DiscoverTvResponse
import com.example.movies.models.network.GenresTvResponse
import com.example.movies.models.repository.MoviesRepositoryImpl
import javax.inject.Inject

class MoviesUseCase @Inject constructor(private val moviesRepositoryImpl: MoviesRepositoryImpl) {

    suspend fun getDiscoverTv(page: Int): ApiResource<DiscoverTvResponse> {
        return moviesRepositoryImpl.getDiscoverTv(page)
    }

    suspend fun getGenresTv(): ApiResource<GenresTvResponse> {
        return moviesRepositoryImpl.getGenresTv()
    }

}