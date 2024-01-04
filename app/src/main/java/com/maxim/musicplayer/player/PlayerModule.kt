package com.maxim.musicplayer.player

import com.maxim.musicplayer.cope.Core
import com.maxim.musicplayer.cope.Module
import com.maxim.musicplayer.player.presentation.PlayerCommunication
import com.maxim.musicplayer.player.presentation.PlayerViewModel

class PlayerModule(private val core: Core) : Module<PlayerViewModel> {
    override fun viewModel() = PlayerViewModel(
        core.sharedStorage(),
        PlayerCommunication.Base(),
        core.mediaPlayerWrapper()
    )
}