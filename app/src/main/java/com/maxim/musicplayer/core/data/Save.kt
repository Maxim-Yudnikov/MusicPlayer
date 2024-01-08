package com.maxim.musicplayer.core.data

interface Save<T> {
    fun save(value: T)
}