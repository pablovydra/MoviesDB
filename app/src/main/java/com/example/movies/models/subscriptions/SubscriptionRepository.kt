package com.example.movies.models.subscriptions

import javax.inject.Inject

class SubscriptionRepository @Inject constructor(private val subscriptionDao: SubscriptionDao) {

    suspend fun getAll() = subscriptionDao.getAll()

    suspend fun insert(subscription: Subscription) = subscriptionDao.insert(subscription)

    suspend fun delete(subscription: Subscription) = subscriptionDao.delete(subscription)

    suspend fun deleteById(id: Int) = subscriptionDao.deleteById(id)

    suspend fun getById(id: Int): Subscription = subscriptionDao.getById(id)

}
