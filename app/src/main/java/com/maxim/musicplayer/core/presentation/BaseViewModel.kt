package com.maxim.musicplayer.core.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class BaseViewModel(private val runAsync: RunAsync = RunAsync.Base()) : ViewModel() {
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    protected fun <T: Any> handle(onBackground: suspend () -> T, onUi: (T) -> Unit) {
        runAsync.handle(viewModelScope, onBackground, onUi)
    }
}