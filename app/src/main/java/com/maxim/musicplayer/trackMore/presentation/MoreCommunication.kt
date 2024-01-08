package com.maxim.musicplayer.trackMore.presentation

import com.maxim.musicplayer.cope.presentation.Communication

interface MoreCommunication: Communication.Mutable<MoreState> {
    class Base: Communication.Regular<MoreState>(), MoreCommunication
}