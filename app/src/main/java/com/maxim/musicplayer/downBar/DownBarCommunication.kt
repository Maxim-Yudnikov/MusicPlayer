package com.maxim.musicplayer.downBar

import com.maxim.musicplayer.core.presentation.Communication

interface DownBarCommunication: Communication.Mutable<DownBarState> {
    class Base: Communication.Regular<DownBarState>(), DownBarCommunication
}