package com.maxim.musicplayer.cope

import android.content.Context

class Core(private val context: Context) {
    private val navigation = Navigation.Base()
    fun navigation() = navigation
    fun contentResolver() = context.contentResolver
}