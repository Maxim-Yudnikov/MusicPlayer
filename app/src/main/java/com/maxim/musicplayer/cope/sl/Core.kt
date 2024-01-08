package com.maxim.musicplayer.cope.sl

import android.content.Context
import com.maxim.musicplayer.cope.ProvideDownBarTrackCommunication
import com.maxim.musicplayer.cope.ProvideManageOrder
import com.maxim.musicplayer.cope.ProvideMediaService
import com.maxim.musicplayer.cope.ProvidePlayerCommunication
import com.maxim.musicplayer.cope.presentation.Navigation
import com.maxim.musicplayer.favoriteList.data.FavoriteListRepository

class Core(private val context: Context, private val provideInstances: ProvideInstances) {
    private val navigation = Navigation.Base()
    fun navigation() = navigation
    fun contentResolverWrapper() = provideInstances.contentResolverWrapper()
    fun manageOrder() = (context.applicationContext as ProvideManageOrder).manageOrder()
    fun provideMediaService() = (context.applicationContext as ProvideMediaService)
    fun downBarTrackCommunication() =
        (context.applicationContext as ProvideDownBarTrackCommunication).downBarTrackCommunication()
    fun playerCommunication() = (context.applicationContext as ProvidePlayerCommunication).playerCommunication()

    private lateinit var favoriteRepository: FavoriteListRepository
    fun favoriteRepository() = favoriteRepository

    fun database() = provideInstances.database()

    fun init() {
        favoriteRepository = FavoriteListRepository.Base(database().dao())
    }
}