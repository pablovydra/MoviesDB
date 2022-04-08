package com.example.movies.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movies.models.dto.ApiResource
import com.example.movies.models.entity.Tv
import com.example.movies.models.usecase.MoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val moviesUseCase: MoviesUseCase) : ViewModel() {

    val showList = MutableLiveData<MutableList<Tv>>(mutableListOf())
    val loading = MutableLiveData<Boolean>()
    val setAdapterOnView = MutableLiveData<Boolean>()

    private val pagination = MutableLiveData<Int>(0)

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    private var page = 0

    init {
        getShows()
    }

    fun getShows() {
        coroutineScope.launch(Dispatchers.IO) {
            val response = pagination.value?.let { _ -> moviesUseCase.getDiscoverTv(++page) }
            when (response?.status) {
                ApiResource.Status.SUCCESS -> {
                    loading.postValue(false)
                    if (response.data?.results?.isNotEmpty() == true) {
                        response.data.let { list -> showList.value?.addAll(list.results) }
                        setAdapterOnView.postValue(true)
                    }
                }
                ApiResource.Status.ERROR -> {
                    loading.postValue(false)
                }
            }
        }
    }

}