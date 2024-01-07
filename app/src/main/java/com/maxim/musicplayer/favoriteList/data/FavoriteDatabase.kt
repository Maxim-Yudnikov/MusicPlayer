package com.maxim.musicplayer.favoriteList.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [AudioRoom::class], version = 1)
@TypeConverters(UriConverter::class)
abstract class FavoriteDatabase: RoomDatabase() {
    abstract fun dao(): FavoriteDao
}