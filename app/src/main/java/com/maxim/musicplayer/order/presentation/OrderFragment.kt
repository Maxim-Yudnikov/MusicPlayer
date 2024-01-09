package com.maxim.musicplayer.order.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.musicplayer.core.presentation.BaseFragment
import com.maxim.musicplayer.databinding.FragmentAudioListBinding

class OrderFragment: BaseFragment<FragmentAudioListBinding, OrderViewModel>() {
    override fun viewModelClass() = OrderViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAudioListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.goBack()
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }
}