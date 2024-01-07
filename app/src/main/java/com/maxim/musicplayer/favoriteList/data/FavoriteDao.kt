package com.maxim.musicplayer.favoriteList.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(track: AudioRoom)

    @Query("SELECT * FROM favorite_tracks")
    fun favoriteTracksLiveData(): LiveData<List<AudioRoom>>

    @Query("SELECT * FROM favorite_tracks")
    fun favoriteTracks(): List<AudioRoom>

    @Query("DELETE FROM favorite_tracks WHERE id IS :id")
    suspend fun removeTrack(id: Long)
}