package com.souvikmondal01.flare.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media")
data class Media(
    @PrimaryKey()
    val id: Int,
    val title: String,
    val posterPath: String,
    val runtime: Int,
    val voteAverage: Double,
    val certification: String,
    val releaseDate: String,
    val originalLanguage: String,
    val mediaType: String,
    val createdAt: Long
)