package com.maxim.musicplayer.audioList.presentation

import com.maxim.musicplayer.cope.Communication

interface ActualTrackPositionCommunication: Communication.Mutable<Int> {
    class Base: Communication.Regular<Int>(), ActualTrackPositionCommunication
}