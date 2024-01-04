package com.maxim.musicplayer.cope

interface Navigation {
    interface Update: Communication.Update<Screen>
    interface Observe: Communication.Observe<Screen>
    interface Mutable: Update, Observe
    class Base: Communication.Single<Screen>(), Mutable
}