package com.maxim.musicplayer.player.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxim.musicplayer.cope.BaseFragment
import com.maxim.musicplayer.databinding.FragmentPlayerBinding
import com.maxim.musicplayer.main.MainActivity

class PlayerFragment : BaseFragment<FragmentPlayerBinding, PlayerViewModel>() {
    override fun viewModelClass() = PlayerViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentPlayerBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observe(this) {
            it.show(
                binding.artImageView,
                binding.titleTextView,
                binding.artistTextView,
                binding.playButton
            )
        }
        val mediaService = (requireActivity() as MainActivity).mediaService()

        binding.playButton.setOnClickListener {
            viewModel.play(mediaService)
        }

        viewModel.init(savedInstanceState == null, mediaService)
    }
}