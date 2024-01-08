package com.maxim.musicplayer.main

import com.maxim.musicplayer.core.sl.Core
import com.maxim.musicplayer.core.sl.Module

class MainModule(private val core: Core): Module<MainViewModel> {
    override fun viewModel() = MainViewModel(
        core.navigation()
    )
}