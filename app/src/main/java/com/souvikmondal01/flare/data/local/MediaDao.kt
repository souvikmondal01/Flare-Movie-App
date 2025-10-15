package com.souvikmondal01.flare.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.souvikmondal01.flare.data.local.model.Media
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedia(media: Media)

    @Query("DELETE FROM media WHERE id = :id")
    suspend fun deleteMediaById(id: Int)

    @Query("SELECT * FROM media ORDER BY createdAt DESC")
    fun getAllMedia(): Flow<List<Media>>

    @Query("SELECT EXISTS(SELECT 1 FROM media WHERE id = :id)")
    fun isMediaAvailable(id: Int): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM media WHERE id = :id)")
    fun isMediaSaved(id: Int): Flow<Boolean>
}