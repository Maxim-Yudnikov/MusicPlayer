package com.maxim.musicplayer.downBar

import com.maxim.musicplayer.cope.Communication

interface DownBarCommunication: Communication.Mutable<DownBarState> {
    class Base: Communication.Regular<DownBarState>(), DownBarCommunication
}