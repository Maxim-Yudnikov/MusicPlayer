package com.maxim.musicplayer.albumList.presentation

import android.widget.TextView
import com.maxim.musicplayer.audioList.presentation.ArtImageView
import com.maxim.musicplayer.audioList.presentation.AudioUi

interface AlbumUi {
    fun same(item: AlbumUi): Boolean
    fun showArt(artImageView: ArtImageView)
    fun showTitle(textView: TextView)
    fun showDescription(textView: TextView)

    data class Base(
        private val id: Long,
        private val title: String,
        private val artist: String,
        private val tracks: List<AudioUi>
    ): AlbumUi {
        override fun same(item: AlbumUi) = item is Base && item.id == id
        override fun showArt(artImageView: ArtImageView) {
            tracks.first().showArt(artImageView, true)
        }

        override fun showTitle(textView: TextView) {
            textView.text = title
        }

        override fun showDescription(textView: TextView) {
            val text = "$artist - ${tracks.size} songs"
            textView.text = text
        }
    }
}