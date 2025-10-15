package com.souvikmondal01.flare.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.souvikmondal01.flare.data.local.model.Media

@Database(version = 1, entities = [Media::class], exportSchema = false)
abstract class MediaDatabase : RoomDatabase() {
    abstract fun getMediaDao(): MediaDao
}