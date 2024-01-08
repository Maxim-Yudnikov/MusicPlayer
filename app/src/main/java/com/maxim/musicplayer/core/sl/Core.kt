package com.maxim.musicplayer.core.sl

import android.content.Context
import com.maxim.musicplayer.core.ProvideDownBarTrackCommunication
import com.maxim.musicplayer.core.ProvideManageOrder
import com.maxim.musicplayer.core.ProvideMediaService
import com.maxim.musicplayer.core.ProvidePlayerCommunication
import com.maxim.musicplayer.core.presentation.Navigation
import com.maxim.musicplayer.favoriteList.data.FavoriteListRepository
import com.maxim.musicplayer.trackMore.presentation.MoreStorage

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

    private val moreStorage = MoreStorage.Base()
    fun moreStorage() = moreStorage

    fun init() {
        favoriteRepository = FavoriteListRepository.Base(database().dao())
    }
}