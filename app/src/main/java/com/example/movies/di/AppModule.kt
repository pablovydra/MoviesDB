package com.example.movies.di

import android.content.Context
import android.support.annotation.NonNull
import androidx.room.Room
import com.example.movies.models.network.RequestInterceptor
import com.example.movies.models.repository.MoviesRepositoryImpl
import com.example.movies.models.services.MoviesApiService
import com.example.movies.models.subscriptions.SubscriptionDatabase
import com.example.movies.models.usecase.MoviesUseCase
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(RequestInterceptor())
        .addNetworkInterceptor(StethoInterceptor())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(@NonNull okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideMoviesApiService(retrofit: Retrofit): MoviesApiService = retrofit.create(MoviesApiService::class.java)

    @Singleton
    @Provides
    fun provideRepository(moviesApiService: MoviesApiService) = MoviesRepositoryImpl(moviesApiService)

    @Singleton
    @Provides
    fun provideUsecase(moviesRepository: MoviesRepositoryImpl) = MoviesUseCase(moviesRepository)

    @Singleton
    @Provides
    fun provideSubscriptionDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        SubscriptionDatabase::class.java,
        "subscriptions"
    ).build()

    @Singleton
    @Provides
    fun provideSubscriptionDao(db: SubscriptionDatabase) = db.subscriptionDao()

}