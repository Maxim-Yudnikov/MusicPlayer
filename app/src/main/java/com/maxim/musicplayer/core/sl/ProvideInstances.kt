package com.maxim.musicplayer.core.sl

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.maxim.musicplayer.audioList.data.ContentResolverWrapper
import com.maxim.musicplayer.audioList.data.MockContentResolverWrapper
import com.maxim.musicplayer.core.data.MockSimpleStorage
import com.maxim.musicplayer.core.data.SimpleStorage
import com.maxim.musicplayer.favoriteList.data.FavoriteDatabase
import com.maxim.musicplayer.player.media.ShuffleOrder

interface ProvideInstances {
    fun database(): FavoriteDatabase
    fun contentResolverWrapper(): ContentResolverWrapper
    fun shuffleOrder(): ShuffleOrder
    fun simpleStorage(): SimpleStorage

    class Release(private val context: Context): ProvideInstances {
        private val database by lazy {
            Room.databaseBuilder(context, FavoriteDatabase::class.java, "favorite_database").build()
        }

        override fun database() = database

        override fun contentResolverWrapper() = ContentResolverWrapper.Base(context.contentResolver)
        override fun shuffleOrder() = ShuffleOrder.Base()
        override fun simpleStorage() = SimpleStorage.Base(context.getSharedPreferences(STORAGE_NAME,
            Application.MODE_PRIVATE
        ))
    }

    class Mock(private val context: Context): ProvideInstances {
        private val database by lazy {
            Room.inMemoryDatabaseBuilder(context, FavoriteDatabase::class.java).build()
        }

        override fun database() = database

        override fun contentResolverWrapper() = MockContentResolverWrapper()
        override fun shuffleOrder() = ShuffleOrder.Mock()
        override fun simpleStorage() = MockSimpleStorage()
    }

    companion object {
        private const val STORAGE_NAME = "MUSIC_PLAYER_STORAGE"
    }
}