package com.maxim.musicplayer.cope

import android.content.Context
import com.maxim.musicplayer.downBar.DownBarRepository
import com.maxim.musicplayer.player.media.ManageOrder
import com.maxim.musicplayer.player.presentation.OpenPlayerStorage

class Core(private val context: Context) {
    private val navigation = Navigation.Base()
    fun navigation() = navigation
    fun contentResolverWrapper() = ContentResolverWrapper.Base(context.contentResolver)
    private val sharedStorage = OpenPlayerStorage.Base()
    fun sharedStorage() = sharedStorage
    private val manageOrder = ManageOrder.Base(
        SimpleStorage.Base(
            context.getSharedPreferences(
                STORAGE_NAME, Context.MODE_PRIVATE
            )
        )
    )
    fun manageOrder() = manageOrder
    private val downBarRepository = DownBarRepository.Base()
    fun downBarRepository() = downBarRepository

    companion object {
        private const val STORAGE_NAME = "MUSIC_PLAYER_STORAGE"
    }
}