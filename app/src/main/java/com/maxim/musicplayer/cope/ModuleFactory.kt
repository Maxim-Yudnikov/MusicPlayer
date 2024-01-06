package com.maxim.musicplayer.cope

import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.audioList.AudioListModule
import com.maxim.musicplayer.audioList.presentation.AudioListViewModel
import com.maxim.musicplayer.downBar.DownBarModule
import com.maxim.musicplayer.downBar.DownBarViewModel
import com.maxim.musicplayer.main.MainModule
import com.maxim.musicplayer.main.MainViewModel
import com.maxim.musicplayer.player.PlayerModule
import com.maxim.musicplayer.player.presentation.PlayerViewModel
import java.lang.IllegalStateException

interface ModuleFactory: ClearViewModel, ProvideViewModel {
    class Base(private val provider: ProvideModule): ModuleFactory {
        private val map = mutableMapOf<Class<out ViewModel>, ViewModel>()
        override fun clear(clasz: Class<out ViewModel>) {
            map.remove(clasz)
        }

        override fun <T : ViewModel> viewModel(clasz: Class<T>): T {
            if (map[clasz] == null)
                map[clasz] = provider.module(clasz).viewModel()
            return map[clasz] as T
        }
    }
}

interface ClearViewModel {
    fun clear(clasz: Class<out ViewModel>)
}
interface ProvideModule {
    fun <T: ViewModel> module(clasz: Class<T>): Module<T>

    class Base(private val core: Core): ProvideModule {
        override fun <T : ViewModel> module(clasz: Class<T>): Module<T> {
            return when (clasz) {
                MainViewModel::class.java -> MainModule(core)
                AudioListViewModel::class.java -> AudioListModule(core)
                PlayerViewModel::class.java -> PlayerModule(core)
                DownBarViewModel::class.java -> DownBarModule(core)
                else -> throw IllegalStateException("unknown viewModel $clasz")
            } as Module<T>
        }
    }
}

interface ProvideViewModel {
    fun <T: ViewModel> viewModel(clasz: Class<T>): T
}