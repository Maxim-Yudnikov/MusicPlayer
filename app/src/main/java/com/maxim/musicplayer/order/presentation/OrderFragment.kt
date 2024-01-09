package com.maxim.musicplayer.order.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.musicplayer.core.presentation.BaseFragment
import com.maxim.musicplayer.databinding.FragmentFavoriteListBinding

class OrderFragment: BaseFragment<FragmentFavoriteListBinding, OrderViewModel>() {
    override fun viewModelClass() = OrderViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFavoriteListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.goBack()
            }
        }
        super.onViewCreated(view, savedInstanceState)

        val adapter = OrderAdapter(object : OrderAdapter.Listener {
            override fun remove(id: Long) {
                viewModel.remove(id)
            }
        })
        binding.favoriteRecyclerView.adapter = adapter

        viewModel.observe(this) {
            it.show(adapter)
        }

        viewModel.init(savedInstanceState == null)
    }
}