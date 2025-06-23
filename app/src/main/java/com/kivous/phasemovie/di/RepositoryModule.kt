package com.kivous.phasemovie.di

import com.kivous.phasemovie.data.repository.MovieRepositoryImpl
import com.kivous.phasemovie.domain.repository.MovieRepository
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