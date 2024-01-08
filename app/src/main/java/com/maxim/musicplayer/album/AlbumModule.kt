package com.maxim.musicplayer.album

import com.maxim.musicplayer.album.presentation.AlbumCommunication
import com.maxim.musicplayer.album.presentation.AlbumViewModel
import com.maxim.musicplayer.core.sl.ClearViewModel
import com.maxim.musicplayer.core.sl.Core
import com.maxim.musicplayer.core.sl.Module

class AlbumModule(private val core: Core, private val clearViewModel: ClearViewModel): Module<AlbumViewModel> {
    override fun viewModel() = AlbumViewModel(
        AlbumCommunication.Base(),
        core.openAlbumStorage(),
        core.manageOrder(),
        core.navigation(),
        clearViewModel
    )
}