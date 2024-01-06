package com.maxim.musicplayer.downBar

import com.maxim.musicplayer.cope.Core
import com.maxim.musicplayer.cope.Module

class DownBarModule(private val core: Core): Module<DownBarViewModel> {
    override fun viewModel() = DownBarViewModel(
        core.downBarTrackCommunication(),
        core.manageOrder(),
        DownBarCommunication.Base(),
        core.navigation()
    )
}