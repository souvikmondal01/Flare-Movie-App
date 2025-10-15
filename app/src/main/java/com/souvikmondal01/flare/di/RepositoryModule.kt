package com.souvikmondal01.flare.di

import com.souvikmondal01.flare.data.repository.MovieRepositoryImpl
import com.souvikmondal01.flare.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieListRepository(
        movieListRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

}