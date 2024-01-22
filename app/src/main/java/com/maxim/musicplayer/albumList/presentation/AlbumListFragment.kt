package com.maxim.musicplayer.albumList.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.maxim.musicplayer.R
import com.maxim.musicplayer.audioList.presentation.RefreshFinish
import com.maxim.musicplayer.core.presentation.BaseFragment
import com.maxim.musicplayer.databinding.FragmentAudioListBinding

class AlbumListFragment: BaseFragment<FragmentAudioListBinding, AlbumListViewModel>(), RefreshFinish {
    override fun viewModelClass() = AlbumListViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAudioListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AlbumListAdapter(object : AlbumListAdapter.Listener {
            override fun open(album: AlbumUi) {
                viewModel.open(album)
            }
        })
        binding.audioRecyclerView.adapter = adapter
        binding.audioRecyclerView.layoutManager = GridLayoutManager(requireContext(), resources.getInteger(
            R.integer.albums_span_count))

        viewModel.observe(this) {
            it.show(adapter)
        }

        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.refresh(this)
        }

        viewModel.init(savedInstanceState == null, this)
    }

    override fun finish() {
        binding.swipeToRefresh.isRefreshing = false
    }
}