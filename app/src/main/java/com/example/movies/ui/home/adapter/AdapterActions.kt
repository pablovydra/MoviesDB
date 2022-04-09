package com.example.movies.ui.home.adapter

import com.example.movies.models.entity.Shows
import com.example.movies.models.entity.Tv

interface AdapterActions {
    fun addToFavorite(tv: Tv, callback: (() -> Unit)? = null)
    fun removeFavorite(tv: Tv, callback: (() -> Unit)? = null)
    fun navigateToItem(show: Shows)
}