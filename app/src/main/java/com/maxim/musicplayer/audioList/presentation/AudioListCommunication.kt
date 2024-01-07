package com.maxim.musicplayer.audioList.presentation

import com.maxim.musicplayer.cope.presentation.Communication

interface AudioListCommunication: Communication.Mutable<AudioListState> {
    class Base: Communication.Regular<AudioListState>(), AudioListCommunication
}