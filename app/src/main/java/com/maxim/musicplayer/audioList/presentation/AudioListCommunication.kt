package com.maxim.musicplayer.audioList.presentation

import com.maxim.musicplayer.core.presentation.Communication

interface AudioListCommunication: Communication.Mutable<AudioListState> {
    class Base: Communication.Regular<AudioListState>(), AudioListCommunication
}