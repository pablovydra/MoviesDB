package com.example.movies.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.models.database.Shows
import com.example.movies.models.database.ShowsRepository
import com.example.movies.models.dto.ApiResource
import com.example.movies.models.entity.Genres
import com.example.movies.models.usecase.MoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviesUseCase: MoviesUseCase,
    private val repository: ShowsRepository,
) : ViewModel() {

    val showList = MutableLiveData<ArrayList<Shows>>(arrayListOf())
    private val genresList = MutableLiveData<List<Genres>>(mutableListOf())
    val subscriptionList = MutableLiveData<List<Shows>>(arrayListOf())

    private val loading = MutableLiveData<Boolean>()
    val setRecommendedList = MutableLiveData<Boolean>()
    val setSubscriptionsList = MutableLiveData<Boolean>()

    val selectedShow = MutableLiveData<Shows>()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    private var page = 0

    init {
        coroutineScope.launch(Dispatchers.IO) {
            getGenres()
        }
    }

    private fun getGenres() {
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

    fun getShows() {
        viewModelScope.launch(Dispatchers.IO) {
            getShowsFlow()
                .onStart { }
                .onCompletion {
                    setRecommendedList.postValue(true)
                }
                .collect {
                    showList.value?.addAll(it)
                    setRecommendedList.postValue(true)
                }
        }
    }

    private fun getGenresFlows(): Flow<List<Genres>> = flow {
        val response = moviesUseCase.getGenresTv()
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

    private fun getShowsFlow(): Flow<List<Shows>> = flow {
        val response = moviesUseCase.getDiscoverTv(++page)
        when (response.status) {
            ApiResource.Status.SUCCESS -> {

                if (response.data?.results != null) {

                    val itemsToAdd = ArrayList<Shows>()

                    for (data in response.data.results) {

                        var genreFinal = ""

                        if (data.genre_ids.isNotEmpty()) {
                            val genderFilter = genresList.value?.filter { it.id == data.genre_ids.first() }
                            if (genderFilter != null) {
                                if (genderFilter.isNotEmpty()) {
                                    genreFinal = genderFilter.first().name
                                }
                            }
                        }

                        val subscribedList = subscriptionList.value?.filter { it.id == data.id }
                        var subscribed = false
                        if (subscribedList != null) {
                            if (subscribedList.isNotEmpty()) {
                                subscribed = true
                            }
                        }

                        itemsToAdd.add(
                            Shows(
                                data.id,
                                data.poster_path,
                                data.overview,
                                data.first_air_date ?: "",
                                genreFinal,
                                data.name,
                                subscribed
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

    fun getSubscriptions() = viewModelScope.launch {
        subscriptionList.value = repository.getAll()
        setSubscriptionsList.postValue(true)
    }

    fun insert(show: Shows) = viewModelScope.launch {
        repository.insert(show)
    }

    fun delete(id: Int) = viewModelScope.launch {
        repository.deleteById(id)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}