package com.maxim.musicplayer.albumList.presentation

import android.widget.TextView
import com.maxim.musicplayer.R
import com.maxim.musicplayer.audioList.presentation.ArtImageView
import com.maxim.musicplayer.audioList.presentation.AudioListAdapter
import com.maxim.musicplayer.audioList.presentation.AudioUi

interface AlbumUi {
    fun same(item: AlbumUi): Boolean
    fun showArt(artImageView: ArtImageView)
    fun showTitle(textView: TextView)
    fun showArtist(textView: TextView)
    fun showDescription(textView: TextView)
    fun showCount(textView: TextView)
    fun showTracks(adapter: AudioListAdapter)

    data class Base(
        private val id: Long,
        private val title: String,
        private val artist: String,
        //todo public field, use in viewModel
        val tracks: List<AudioUi>
    ): AlbumUi {
        override fun same(item: AlbumUi) = item is Base && item.id == id
        override fun showArt(artImageView: ArtImageView) {
            tracks.first().showArt(artImageView, false)
        }

        override fun showTitle(textView: TextView) {
            textView.text = title
        }

        override fun showArtist(textView: TextView) {
            textView.text = artist
        }

        override fun showDescription(textView: TextView) {
            val text = textView.context.getString(R.string.arist_and_tracks, artist, tracks.size.toString())
            textView.text = text
        }

        override fun showCount(textView: TextView) {
            val text = textView.context.getString(R.string.tracks, tracks.size.toString())
            textView.text = text
        }

        override fun showTracks(adapter: AudioListAdapter) {
            adapter.update(tracks, -1)
        }
    }
}