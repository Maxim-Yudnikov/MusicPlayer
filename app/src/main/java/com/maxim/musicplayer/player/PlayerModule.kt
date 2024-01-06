package com.maxim.musicplayer.player

import com.maxim.musicplayer.cope.Core
import com.maxim.musicplayer.cope.Module
import com.maxim.musicplayer.cope.ProvideMediaService
import com.maxim.musicplayer.player.presentation.PlayerViewModel

class PlayerModule(private val core: Core, private val provideMediaService: ProvideMediaService) : Module<PlayerViewModel> {
    override fun viewModel() = PlayerViewModel(
        core.downBarTrackCommunication(),
        core.playerCommunication(),
        core.manageOrder(),
        provideMediaService
    )
}