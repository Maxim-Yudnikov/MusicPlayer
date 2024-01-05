package com.maxim.musicplayer.cope

import android.content.Context
import com.maxim.musicplayer.player.presentation.OpenPlayerStorage

class Core(private val context: Context) {
    private val navigation = Navigation.Base()
    fun navigation() = navigation

    //todo contentResolver wrapper
    fun contentResolver() = context.contentResolver
    private val sharedStorage = OpenPlayerStorage.Base()
    fun sharedStorage() = sharedStorage
    fun manageOrder() = (context.applicationContext as ManageOrder)
}