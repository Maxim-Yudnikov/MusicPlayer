package com.maxim.musicplayer.cope.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface RunAsync {
    fun <T: Any> handle(
        coroutineScope: CoroutineScope,
        onBackground: suspend () -> T,
        onUi: (T) -> Unit
    )

    class Base: RunAsync {
        override fun <T : Any> handle(
            coroutineScope: CoroutineScope,
            onBackground: suspend () -> T,
            onUi: (T) -> Unit
        ) {
            coroutineScope.launch(Dispatchers.IO) {
                val result = onBackground.invoke()
                withContext(Dispatchers.Main) {
                    onUi.invoke(result)
                }
            }
        }
    }
}