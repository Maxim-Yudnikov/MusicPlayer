package com.maxim.musicplayer.audioList.presentation

import android.net.Uri
import android.widget.TextView

abstract class AudioUi {
    abstract fun same(item: AudioUi): Boolean
    abstract fun showTitle(textView: TextView)
    abstract fun showArtistAndAlbum(textView: TextView)
    data class Base(
        private val id: Long,
        private val title: String,
        private val artist: String,
        private val duration: String,
        private val album: String,
        private val uri: Uri
    ) : AudioUi() {
        override fun same(item: AudioUi) = item is Base && item.id == id
        override fun showTitle(textView: TextView) {
            textView.text = title
        }

        override fun showArtistAndAlbum(textView: TextView) {
            val s = "$artist $album"
            textView.text = s
        }
    }
}