package com.maxim.musicplayer.downBar

import com.maxim.musicplayer.cope.Core
import com.maxim.musicplayer.cope.Module

class DownBarModule(private val core: Core): Module<DownBarViewModel> {
    override fun viewModel() = DownBarViewModel(
        core.downBarRepository(),
        core.sharedStorage(),
        DownBarCommunication.Base(),
        core.navigation()
    )
}