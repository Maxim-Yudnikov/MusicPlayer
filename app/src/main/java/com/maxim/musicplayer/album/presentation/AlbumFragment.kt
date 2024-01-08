package com.maxim.musicplayer.album.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.musicplayer.audioList.presentation.AudioListAdapter
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.core.presentation.BaseFragment
import com.maxim.musicplayer.databinding.FragmentAlbumBinding
import com.maxim.musicplayer.player.media.MediaService

class AlbumFragment: BaseFragment<FragmentAlbumBinding, AlbumViewModel>() {
    override fun viewModelClass() = AlbumViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAlbumBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.goBack()
            }
        }
        super.onViewCreated(view, savedInstanceState)

        val adapter = AlbumAdapter(object : AudioListAdapter.Listener {
            override fun open(audioUi: AudioUi, position: Int, mediaService: MediaService) {
                viewModel.open(audioUi, position, mediaService)
            }

            override fun more(audioUi: AudioUi) {
                TODO("Not yet implemented")
            }
        })
        binding.favoriteRecyclerView.adapter = adapter

        viewModel.observe(this) {
            it.show(adapter)
        }

        viewModel.observePosition(this) {
            viewModel.setPosition(it.first, it.second)
        }

        viewModel.init(savedInstanceState == null)
    }
}