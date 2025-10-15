package com.souvikmondal01.flare.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.souvikmondal01.flare.data.local.MediaDao
import com.souvikmondal01.flare.data.local.MediaDatabase
import com.souvikmondal01.flare.data.remote.MovieApi
import com.souvikmondal01.flare.performance.FirebasePerformanceTracker
import com.souvikmondal01.flare.performance.PerformanceTracker
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun providesMovieApi(): MovieApi =
        Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(MovieApi.BASE_URL).build().create(MovieApi::class.java)

    @Provides
    @Singleton
    fun providesFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun providePerformanceTracker(): PerformanceTracker = FirebasePerformanceTracker()

    @Provides
    @Singleton
    fun provideMediaDB(@ApplicationContext context: Context): MediaDatabase =
        Room.databaseBuilder(
            context,
            MediaDatabase::class.java,
            "media_db.db"
        ).build()

    @Singleton
    @Provides
    fun provideMediaDao(mediaDatabase: MediaDatabase): MediaDao =
        mediaDatabase.getMediaDao()

}