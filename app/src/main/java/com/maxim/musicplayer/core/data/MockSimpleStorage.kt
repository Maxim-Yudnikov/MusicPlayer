package com.maxim.musicplayer.core.data

import com.maxim.musicplayer.media.LoopState

class MockSimpleStorage: SimpleStorage {
    override fun save(key: String, value: Boolean) = Unit

    override fun save(key: String, value: LoopState) = Unit

    override fun read(key: String, default: Boolean) = false

    override fun read(key: String, default: LoopState) = LoopState.Base
}