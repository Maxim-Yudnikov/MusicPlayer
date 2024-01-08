package com.maxim.musicplayer.player.presentation

import com.maxim.musicplayer.core.presentation.Communication

interface PlayerCommunication: Communication.Mutable<PlayerState> {
    class Base: Communication.Regular<PlayerState>(), PlayerCommunication
}