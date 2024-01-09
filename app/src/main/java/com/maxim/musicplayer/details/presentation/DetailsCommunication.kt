package com.maxim.musicplayer.details.presentation

import com.maxim.musicplayer.core.presentation.Communication

interface DetailsCommunication: Communication.Mutable<DetailsState> {
    class Base: Communication.Regular<DetailsState>(), DetailsCommunication
}