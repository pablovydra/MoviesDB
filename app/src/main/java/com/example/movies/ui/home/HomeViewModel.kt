package com.example.movies.ui.home

import androidx.lifecycle.LiveData
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviesUseCase: MoviesUseCase,
    private val repository: ShowsRepository,
) : ViewModel() {

    private val _showList = MutableLiveData<ArrayList<Shows>>()
    val showList: LiveData<ArrayList<Shows>>
        get() = _showList

    private val genresList: ArrayList<Genres> = arrayListOf()

    private val _setShowList = MutableLiveData<Boolean>()
    val setShowList: LiveData<Boolean>
        get() = _setShowList

    private val _subscriptionList = MutableLiveData<List<Shows>>()
    val subscriptionList: LiveData<List<Shows>>
        get() = _subscriptionList

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _selectedShow = MutableLiveData<Shows>()
    val selectedShow: LiveData<Shows>
        get() = _selectedShow

    fun setSelectedShow(show: Shows) {
        _selectedShow.value = show
    }

    private var page = 0

    init {
        _showList.value = arrayListOf()
        _subscriptionList.value = arrayListOf()
        _loading.value = true

        getGenres()
    }

    private fun getGenres() {
        viewModelScope.launch(Dispatchers.IO) {
            getGenresFlow()
                .onCompletion {
                    getShows()
                }
                .collect {
                    genresList.add(it)
                }
        }
    }

    fun getShows() {
        viewModelScope.launch(Dispatchers.IO) {
            getShowsFlow()
                .onCompletion {
                    _setShowList.postValue(true)
                    _loading.postValue(false)
                }
                .map {
                    setFormatData(it)
                }
                .collect {
                    _showList.value?.add(it)
                }
        }
    }

    private fun setFormatData(tv: Tv): Shows {
        return Shows(
            tv.id,
            tv.poster_path,
            tv.overview,
            tv.first_air_date ?: "",
            getGenreByString(tv.genre_ids),
            tv.name,
            getSubscriptionStatus(tv.id)
        )
    }

    private fun getGenreByString(ids: List<Int>): String {
        var genreFinal = ""
        if (ids.isNotEmpty()) {
            val genderFilter = genresList.filter { it.id == ids.first() }
            genreFinal = if (genderFilter.isNotEmpty()) {
                genderFilter.first().name
            } else {
                ""
            }
        }
        return genreFinal
    }

    private fun getSubscriptionStatus(id: Int): Boolean {
        var isSubscribed = false
        val subscribedList = _subscriptionList.value?.filter { it.id == id }
        isSubscribed = !subscribedList.isNullOrEmpty()
        return isSubscribed
    }

    private fun getGenresFlow(): Flow<Genres> = flow {
        val response = moviesUseCase.getGenresTv()
        when (response.status) {
            ApiResource.Status.SUCCESS -> {
                response.data?.genres?.let {
                    it.forEach { genre ->
                        emit(genre)
                    }
                }
            }
        }
    }

    private fun getShowsFlow(): Flow<Tv> {
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
            }
        }
    }

    fun getSubscriptions() = viewModelScope.launch {
        _subscriptionList.value = repository.getAll()
    }

    fun insert(show: Shows) = viewModelScope.launch {
        repository.insert(show)
    }

    fun delete(id: Int) = viewModelScope.launch {
        repository.deleteById(id)
    }

}