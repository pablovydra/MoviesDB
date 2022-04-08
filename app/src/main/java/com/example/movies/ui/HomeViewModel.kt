package com.example.movies.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.models.dto.ApiResource
import com.example.movies.models.network.DiscoverTvResponse
import com.example.movies.models.usecase.MoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val moviesUseCase: MoviesUseCase) : ViewModel() {

    val discoverTv = MutableLiveData<DiscoverTvResponse>()
    val loading = MutableLiveData<Boolean>(true)
    val setAdapterOnView = MutableLiveData<Boolean>()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    init {

    }

    fun getShows() {
        viewModelScope.launch(Dispatchers.IO) {

            var response = moviesUseCase.getDiscoverTv(1)
            when (response.status) {
                ApiResource.Status.SUCCESS -> {
                    loading.postValue(false)

                    discoverTv.postValue(response.data!!)
                    Log.i("sky", "getShows: ${response.data!!.total_pages}")
                }
                ApiResource.Status.ERROR -> {
                    loading.postValue(false)
                }
            }
        }
    }

}