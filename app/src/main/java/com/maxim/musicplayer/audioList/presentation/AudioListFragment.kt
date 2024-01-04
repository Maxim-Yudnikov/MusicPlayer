package com.maxim.musicplayer.audioList.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxim.musicplayer.cope.BaseFragment
import com.maxim.musicplayer.databinding.FragmentAudioListBinding

class AudioListFragment : BaseFragment<FragmentAudioListBinding, AudioListViewModel>() {
    override fun viewModelClass() = AudioListViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAudioListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AudioListAdapter()
        binding.audioRecyclerView.adapter = adapter

        viewModel.observe(this) {
            it.showList(adapter)
        }

        viewModel.init(savedInstanceState == null)
    }
}