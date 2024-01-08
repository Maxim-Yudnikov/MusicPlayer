package com.maxim.musicplayer.core.data

import android.content.SharedPreferences
import com.maxim.musicplayer.player.media.LoopState
import java.lang.IllegalStateException

interface SimpleStorage {
    fun save(key: String, value: Boolean)
    fun read(key: String, default: Boolean): Boolean

    fun save(key: String, value: LoopState)
    fun read(key: String, default: LoopState): LoopState

    class Base(private val sharedPreferences: SharedPreferences): SimpleStorage {
        override fun save(key: String, value: Boolean) {
            sharedPreferences.edit().putBoolean(key, value).apply()
        }

        override fun read(key: String, default: Boolean) =
            sharedPreferences.getBoolean(key, default)

        override fun save(key: String, value: LoopState) {
            sharedPreferences.edit().putInt(key, value.valueInStorage()).apply()
        }

        private val statesList = listOf(LoopState.Base, LoopState.LoopOrder, LoopState.LoopTrack)
        override fun read(key: String, default: LoopState): LoopState {
            val value = sharedPreferences.getInt(key, default.valueInStorage())
            statesList.forEach {
                if (it.valueInStorage() == value) return it
            }
            throw IllegalStateException("loop state now found")
        }
    }
}