package com.maxim.musicplayer.cope.sl

import android.content.Context
import androidx.room.Room
import com.maxim.musicplayer.audioList.data.ContentResolverWrapper
import com.maxim.musicplayer.cope.ProvideDownBarTrackCommunication
import com.maxim.musicplayer.cope.ProvideManageOrder
import com.maxim.musicplayer.cope.ProvideMediaService
import com.maxim.musicplayer.cope.ProvidePlayerCommunication
import com.maxim.musicplayer.cope.presentation.Navigation
import com.maxim.musicplayer.favoriteList.data.FavoriteDatabase
import com.maxim.musicplayer.favoriteList.data.FavoriteListRepository

class Core(private val context: Context) {
    private val navigation = Navigation.Base()
    fun navigation() = navigation
    fun contentResolverWrapper() = ContentResolverWrapper.Base(context.contentResolver)
    fun manageOrder() = (context.applicationContext as ProvideManageOrder).manageOrder()
    fun provideMediaService() = (context.applicationContext as ProvideMediaService)
    fun downBarTrackCommunication() =
        (context.applicationContext as ProvideDownBarTrackCommunication).downBarTrackCommunication()
    fun playerCommunication() = (context.applicationContext as ProvidePlayerCommunication).playerCommunication()

    private lateinit var favoriteRepository: FavoriteListRepository
    fun favoriteRepository() = favoriteRepository

    private val database by lazy {
        Room.databaseBuilder(context, FavoriteDatabase::class.java, "favorite_database").build()
    }
    fun dao() = database.dao()

    fun init() {
        favoriteRepository = FavoriteListRepository.Base(dao())
    }
}