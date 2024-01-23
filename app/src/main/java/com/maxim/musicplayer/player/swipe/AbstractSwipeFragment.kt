package com.maxim.musicplayer.player.swipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxim.musicplayer.core.presentation.BaseFragment
import com.maxim.musicplayer.databinding.PlayerArtLayoutBinding
import com.maxim.musicplayer.player.swipe.middle.SwipeViewModel

abstract class AbstractSwipeFragment(private val pos: Int): BaseFragment<PlayerArtLayoutBinding, SwipeViewModel>() {
    override fun viewModelClass() = SwipeViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        PlayerArtLayoutBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observe(this) {
            when (pos) {
                0 -> it.first?.showArt(binding.artImageView, true)
                1 -> it.second?.showArt(binding.artImageView, true)
                2 -> it.third?.showArt(binding.artImageView, true)
            }
        }
    }
}