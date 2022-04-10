package com.example.movies.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.models.database.Shows
import com.example.movies.models.database.ShowsRepository
import com.example.movies.models.dto.ApiResource
import com.example.movies.models.entity.Genres
import com.example.movies.models.entity.Tv
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

    val loading = MutableLiveData<Boolean>()

    val setRecommendedList = MutableLiveData<Boolean>()
    val setSubscriptionsList = MutableLiveData<Boolean>()

    val selectedShow = MutableLiveData<Shows>()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    private var page = 0

    init {
        coroutineScope.launch(Dispatchers.IO) {
            loading.postValue(true)
            getGenres()
        }
    }

    private fun getGenres() {
        viewModelScope.launch(Dispatchers.IO) {
            getGenresFlow()
                .onCompletion {
                    getShows()
                }
                .collect {
                    genresList.postValue(it)
                }
        }
    }

    fun getShows() {
        coroutineScope.launch(Dispatchers.IO) {
            getShowsByFlow()
                .onCompletion {
                    loading.postValue(false)
                    setRecommendedList.postValue(true)
                }
                .map {
                    setFormatData(it)
                }
                .collect {
                    showList.value?.add(it)
                }
        }
    }

    private fun setFormatData(tv: Tv): Shows {
        return Shows(tv.id,
            tv.poster_path,
            tv.overview,
            tv.first_air_date ?: "",
            getGenreByString(tv.genre_ids),
            tv.name,
            getSubscriptionStatus(tv.id))
    }

    private fun getGenreByString(ids: List<Int>): String {
        var genreFinal = ""
        if (ids.isNotEmpty()) {
            val genderFilter = genresList.value?.filter { it.id == ids.first() }
            if (genderFilter != null) {
                genreFinal = if (genderFilter.isNotEmpty()) {
                    genderFilter.first().name
                } else {
                    ""
                }
            }
        }
        return genreFinal
    }

    private fun getSubscriptionStatus(id: Int): Boolean {
        var isSubscribed = false
        val subscribedList = subscriptionList.value?.filter { it.id == id }
        isSubscribed = !subscribedList.isNullOrEmpty()
        return isSubscribed
    }

    private fun getGenresFlow(): Flow<List<Genres>> = flow {
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

    private fun getShowsByFlow(): Flow<Tv> {
        return flow {
            val response = moviesUseCase.getDiscoverTv(++page)
            when (response.status) {
                ApiResource.Status.SUCCESS -> {
                    response.data?.results.let {
                        it?.forEach { tv ->
                            emit(tv)
                        }
                    }
                }
                ApiResource.Status.ERROR -> {

                }
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