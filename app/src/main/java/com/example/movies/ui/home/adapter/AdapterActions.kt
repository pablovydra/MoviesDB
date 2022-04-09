package com.example.movies.ui.home.adapter

import com.example.movies.models.database.Shows
import com.example.movies.models.entity.Tv

interface AdapterActions {
    fun addSubscribe(show: Shows, callback: (() -> Unit)? = null)
    fun deleteSubscribe(show: Shows, callback: (() -> Unit)? = null)
    fun navigateToItem(show: Shows)
}