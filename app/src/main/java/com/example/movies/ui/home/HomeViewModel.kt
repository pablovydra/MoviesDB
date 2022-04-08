package com.example.movies.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.models.dto.ApiResource
import com.example.movies.models.entity.Genres
import com.example.movies.models.entity.Shows
import com.example.movies.models.usecase.MoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val moviesUseCase: MoviesUseCase) : ViewModel() {

    val showList = MutableLiveData<ArrayList<Shows>>(arrayListOf())
    private val genresList = MutableLiveData<List<Genres>>(mutableListOf())
    private val loading = MutableLiveData<Boolean>()
    val setAdapterOnView = MutableLiveData<Boolean>()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    private var page = 0

    init {
        coroutineScope.launch(Dispatchers.IO) {
            getGenres()
        }
    }

    fun getShows() {
        viewModelScope.launch(Dispatchers.IO) {
            getShowsFlow()
                .onStart { }
                .onCompletion {
                    setAdapterOnView.postValue(true)
                }
                .collect {
                    showList.value?.addAll(it)
                    setAdapterOnView.postValue(true)
                }
        }
    }

    fun getGenres() {
        viewModelScope.launch(Dispatchers.IO) {
            getGenresFlows()
                .onStart {
                    loading.postValue(true)
                }
                .onCompletion {
                    getShows()
                }
                .collect {
                    genresList.postValue(it)
                }
        }
    }

    fun getGenresFlows(): Flow<List<Genres>> = flow {
        var response = moviesUseCase.getGenresTv()
        when (response.status) {
            ApiResource.Status.SUCCESS -> {
                response.data?.genres?.let {
                    emit(it)
                }
            }
            ApiResource.Status.ERROR -> {
            }
        }
    }

    fun getShowsFlow(): Flow<List<Shows>> = flow {
        var response = moviesUseCase.getDiscoverTv(++page)
        when (response.status) {
            ApiResource.Status.SUCCESS -> {

                if (response.data?.results != null) {

                    var itemsToAdd = ArrayList<Shows>()

                    for (data in response.data!!.results) {

                        var genreFinal = ""

                        if (data.genre_ids.isNotEmpty()) {
                            val genderFilter = genresList.value?.filter { it.id == data.genre_ids.first() }
                            if (genderFilter != null) {
                                if (genderFilter.isNotEmpty()) {
                                    genreFinal = genderFilter?.first()?.name
                                }
                            }
                        }

                        itemsToAdd.add(
                            Shows(
                                data.id,
                                data.poster_path,
                                data.overview,
                                data.first_air_date ?: "",
                                data.genre_ids,
                                genreFinal,
                                data.name,
                                false
                            )
                        )
                    }
                    emit(itemsToAdd)
                }

            }
            ApiResource.Status.ERROR -> {
            }
        }
    }

}