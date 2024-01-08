package com.maxim.musicplayer.trackMore.presentation

import com.maxim.musicplayer.core.presentation.Communication

interface MoreCommunication: Communication.Mutable<MoreState> {
    class Base: Communication.Regular<MoreState>(), MoreCommunication
}