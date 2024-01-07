package com.maxim.musicplayer.favoriteList.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxim.musicplayer.audioList.presentation.AudioListAdapter
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.cope.presentation.BaseFragment
import com.maxim.musicplayer.databinding.FragmentFavoriteListBinding
import com.maxim.musicplayer.player.media.MediaService

class FavoriteListFragment: BaseFragment<FragmentFavoriteListBinding, FavoriteListViewModel>() {
    override fun viewModelClass() = FavoriteListViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFavoriteListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = AudioListAdapter(object : AudioListAdapter.Listener {
            override fun open(audioUi: AudioUi, position: Int, mediaService: MediaService) {
                TODO("Not yet implemented")
            }
        })
        binding.favoriteRecyclerView.adapter = adapter

        viewModel.observe(this) {
            it.showList(adapter)
        }

        viewModel.init(savedInstanceState == null)
    }
}