package com.maxim.musicplayer.cope

import android.content.SharedPreferences

interface SimpleStorage {
    fun save(key: String, value: Boolean)
    fun read(key: String, default: Boolean): Boolean

    class Base(private val sharedPreferences: SharedPreferences): SimpleStorage {
        override fun save(key: String, value: Boolean) {
            sharedPreferences.edit().putBoolean(key, value).apply()
        }

        override fun read(key: String, default: Boolean) =
            sharedPreferences.getBoolean(key, default)
    }
}