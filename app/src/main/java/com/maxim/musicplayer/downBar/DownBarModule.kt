package com.maxim.musicplayer.downBar

import com.maxim.musicplayer.cope.sl.Core
import com.maxim.musicplayer.cope.sl.Module

class DownBarModule(private val core: Core): Module<DownBarViewModel> {
    override fun viewModel() = DownBarViewModel(
        core.downBarTrackCommunication(),
        DownBarCommunication.Base(),
        core.navigation()
    )
}