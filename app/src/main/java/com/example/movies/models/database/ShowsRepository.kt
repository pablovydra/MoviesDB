package com.example.movies.models.database

import javax.inject.Inject

class ShowsRepository @Inject constructor(private val showsDao: ShowsDao) {

    suspend fun getAll() = showsDao.getAll()

    suspend fun insert(shows: Shows) = showsDao.insert(shows)

    suspend fun delete(shows: Shows) = showsDao.delete(shows)

    suspend fun deleteById(id: Int) = showsDao.deleteById(id)

    suspend fun getById(id: Int): Shows = showsDao.getById(id)

}