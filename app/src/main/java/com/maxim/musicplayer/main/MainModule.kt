package com.maxim.musicplayer.main

import com.maxim.musicplayer.cope.sl.Core
import com.maxim.musicplayer.cope.sl.Module

class MainModule(private val core: Core): Module<MainViewModel> {
    override fun viewModel() = MainViewModel(
        core.navigation()
    )
}