package com.maxim.musicplayer.albumList

import com.maxim.musicplayer.albumList.data.AlbumListRepository
import com.maxim.musicplayer.albumList.data.AlbumMapperDomainToUi
import com.maxim.musicplayer.albumList.presentation.AlbumListCommunication
import com.maxim.musicplayer.albumList.presentation.AlbumListViewModel
import com.maxim.musicplayer.audioList.domain.MapperDomainToUi
import com.maxim.musicplayer.core.sl.Core
import com.maxim.musicplayer.core.sl.Module

class AlbumListModule(private val core: Core): Module<AlbumListViewModel> {
    override fun viewModel() = AlbumListViewModel(
        AlbumListCommunication.Base(),
        AlbumListRepository.Base(core.tracksCacheDataSource()),
        core.openAlbumStorage(),
        AlbumMapperDomainToUi(MapperDomainToUi()),
        core.favoriteRepository(),
        core.navigation()
    )
}