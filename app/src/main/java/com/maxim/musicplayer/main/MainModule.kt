package com.maxim.musicplayer.main

import com.maxim.musicplayer.cope.Core
import com.maxim.musicplayer.cope.Module

class MainModule(private val core: Core): Module<MainViewModel> {
    override fun viewModel() = MainViewModel(
        core.navigation()
    )
}