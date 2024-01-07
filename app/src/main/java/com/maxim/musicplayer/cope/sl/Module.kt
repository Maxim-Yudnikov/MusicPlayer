package com.maxim.musicplayer.cope.sl

import androidx.lifecycle.ViewModel

interface Module<T: ViewModel> {
    fun viewModel(): T
}