package com.maxim.musicplayer.cope

import android.content.Context

class Core(private val context: Context) {
    private val navigation = Navigation.Base()
    fun navigation() = navigation
    fun contentResolverWrapper() = ContentResolverWrapper.Base(context.contentResolver)
    fun manageOrder() = (context.applicationContext as ProvideManageOrder).manageOrder()
    fun provideMediaService() = (context.applicationContext as ProvideMediaService)
    fun downBarTrackCommunication() =
        (context.applicationContext as ProvideDownBarTrackCommunication).downBarTrackCommunication()
    fun playerCommunication() = (context.applicationContext as ProvidePlayerCommunication).playerCommunication()
}