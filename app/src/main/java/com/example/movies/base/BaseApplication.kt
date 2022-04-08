package com.example.movies.base

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {

    companion object {
        lateinit var instance: BaseApplication
        fun getApplicationState(context: Context): BaseApplication {
            return context.applicationContext as BaseApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}