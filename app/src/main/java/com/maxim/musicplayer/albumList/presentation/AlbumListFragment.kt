package com.maxim.musicplayer.albumList.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import com.maxim.musicplayer.audioList.presentation.AudioListViewModel
import com.maxim.musicplayer.cope.presentation.BaseFragment
import com.maxim.musicplayer.databinding.FragmentAudioListBinding

class AlbumListFragment: BaseFragment<FragmentAudioListBinding, AudioListViewModel>() {
    override fun viewModelClass() = AudioListViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAudioListBinding.inflate(inflater, container, false)
}