package com.example.movies.models.services

import com.example.movies.models.network.DiscoverTvResponse
import com.example.movies.models.network.GenresTvResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiService {
    @GET("/3/discover/tv?language=en-US&sort_by=popularity.desc")
    suspend fun getDiscoverTv(@Query("page") page: Int): Response<DiscoverTvResponse>

    @GET("/3/genre/tv/list?language=en-US")
    suspend fun getGenresTv(): Response<GenresTvResponse>
}