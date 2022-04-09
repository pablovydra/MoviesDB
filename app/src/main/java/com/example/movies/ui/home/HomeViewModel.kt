package com.example.movies.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import com.example.movies.models.dto.ApiResource
import com.example.movies.models.entity.Genres
import com.example.movies.models.entity.Shows
import com.example.movies.models.subscriptions.Subscription
import com.example.movies.models.subscriptions.SubscriptionRepository
import com.example.movies.models.usecase.MoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val moviesUseCase: MoviesUseCase, private val repository: SubscriptionRepository) : ViewModel() {

    val showList = MutableLiveData<ArrayList<Shows>>(arrayListOf())
    private val genresList = MutableLiveData<List<Genres>>(mutableListOf())
    val subscriptionList = MutableLiveData<List<Subscription>>(arrayListOf())

    val subscriptionsIsEmpty = MutableLiveData<Boolean>(true)

    private val loading = MutableLiveData<Boolean>()
    val setAdapterOnView = MutableLiveData<Boolean>()

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
                    setAdapterOnView.postValue(true)
                }
                .collect {
                    showList.value?.addAll(it)
                    setAdapterOnView.postValue(true)
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

    fun getSubscriptions() = viewModelScope.launch {
        subscriptionList.value = repository.getAll()
    }

    fun insert(show: Shows) = viewModelScope.launch {
        var genreFinal = ""
        if (show.genre_ids?.isNotEmpty() == true) {
            val genderFilter = genresList.value?.filter { it.id == show.genre_ids.first() }
            if (genderFilter != null) {
                if (genderFilter.isNotEmpty()) {
                    genreFinal = genderFilter.first().name
                }
            }
        }
        repository.insert(
            Subscription(
                show.id,
                show.poster_path,
                show.overview,
                show.first_air_date,
                genreFinal,
                show.name,
                show.subscribed
            )
        )
    }

    fun delete(id: Int) = viewModelScope.launch {
        repository.deleteById(id)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}