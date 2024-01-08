package com.maxim.musicplayer.cope.sl

import android.content.Context
import androidx.room.Room
import com.maxim.musicplayer.audioList.data.ContentResolverWrapper
import com.maxim.musicplayer.audioList.data.MockContentResolverWrapper
import com.maxim.musicplayer.favoriteList.data.FavoriteDatabase

interface ProvideInstances {
    fun database(): FavoriteDatabase
    fun contentResolverWrapper(): ContentResolverWrapper

    class Release(private val context: Context): ProvideInstances {
        private val database by lazy {
            Room.databaseBuilder(context, FavoriteDatabase::class.java, "favorite_database").build()
        }

        override fun database() = database

        override fun contentResolverWrapper() = ContentResolverWrapper.Base(context.contentResolver)
    }

    class Mock(private val context: Context): ProvideInstances {
        private val database by lazy {
            Room.inMemoryDatabaseBuilder(context, FavoriteDatabase::class.java).build()
        }

        override fun database() = database

        override fun contentResolverWrapper() = MockContentResolverWrapper()
    }
}