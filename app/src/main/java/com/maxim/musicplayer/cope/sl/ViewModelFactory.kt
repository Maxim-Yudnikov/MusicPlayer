package com.maxim.musicplayer.cope.sl

import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.audioList.AudioListModule
import com.maxim.musicplayer.audioList.presentation.AudioListViewModel
import com.maxim.musicplayer.downBar.DownBarModule
import com.maxim.musicplayer.downBar.DownBarViewModel
import com.maxim.musicplayer.main.MainModule
import com.maxim.musicplayer.main.MainViewModel
import com.maxim.musicplayer.player.PlayerModule
import com.maxim.musicplayer.player.presentation.PlayerViewModel

interface ViewModelFactory : ClearViewModel,
    ProvideViewModel {
    class Base(private val provider: ProvideViewModel) : ViewModelFactory {
        private val map = mutableMapOf<Class<out ViewModel>, ViewModel>()
        override fun clear(clasz: Class<out ViewModel>) {
            map.remove(clasz)
        }

        override fun <T : ViewModel> viewModel(clasz: Class<T>): T {
            if (map[clasz] == null)
                map[clasz] = provider.viewModel(clasz)
            return map[clasz] as T
        }
    }

    object Empty: ViewModelFactory {
        override fun clear(clasz: Class<out ViewModel>) =
            throw IllegalStateException("empty used")

        override fun <T : ViewModel> viewModel(clasz: Class<T>) =
            throw IllegalStateException("empty used")
    }
}

interface ClearViewModel {
    fun clear(clasz: Class<out ViewModel>)
}

interface ProvideViewModel {
    fun <T : ViewModel> viewModel(clasz: Class<T>): T

    class Base(private val core: Core, private val clearViewModel: ClearViewModel) :
        ProvideViewModel {

        override fun <T : ViewModel> viewModel(clasz: Class<T>): T {
            return when (clasz) {
                MainViewModel::class.java -> MainModule(core)
                AudioListViewModel::class.java -> AudioListModule(core)
                PlayerViewModel::class.java -> PlayerModule(
                    core,
                    core.provideMediaService(),
                    clearViewModel
                )
                DownBarViewModel::class.java -> DownBarModule(core)
                else -> throw IllegalStateException("unknown viewModel $clasz")
            }.viewModel() as T
        }
    }
}