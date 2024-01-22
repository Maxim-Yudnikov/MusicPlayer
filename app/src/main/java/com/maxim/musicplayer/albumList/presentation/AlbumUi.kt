package com.maxim.musicplayer.albumList.presentation

import android.widget.TextView
import com.maxim.musicplayer.R
import com.maxim.musicplayer.album.presentation.AlbumAdapter
import com.maxim.musicplayer.audioList.presentation.ArtImageView
import com.maxim.musicplayer.audioList.presentation.AudioListAdapter
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.media.MediaService
import com.maxim.musicplayer.media.OrderType

abstract class AlbumUi {
    abstract fun same(item: AlbumUi): Boolean
    open fun showArt(artImageView: ArtImageView) = Unit
    open fun showTitle(textView: TextView) = Unit
    open fun showArtist(textView: TextView) = Unit
    open fun showDescription(textView: TextView) = Unit
    open fun showCount(textView: TextView) = Unit
    open fun showTracks(adapter: AudioListAdapter) = Unit
    open fun updateTracks(list: List<AudioUi>): AlbumUi = Empty
    open fun open(mediaService: MediaService, track: AudioUi, position: Int, orderType: OrderType) = Unit
    open fun updateAdapter(adapter: AlbumAdapter, position: Int) = Unit

    data class Base(
        private val id: Long,
        private val title: String,
        private val artist: String,
        private val tracks: List<AudioUi>
    ) : AlbumUi() {
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
            val text = textView.context.getString(
                R.string.arist_and_tracks,
                artist,
                tracks.size.toString()
            )
            textView.text = text
        }

        override fun showCount(textView: TextView) {
            val text = textView.context.getString(R.string.tracks, tracks.size.toString())
            textView.text = text
        }

        override fun showTracks(adapter: AudioListAdapter) {
            adapter.update(tracks, -1)
        }

        override fun updateTracks(list: List<AudioUi>): AlbumUi {
            val newList = ArrayList(tracks.map { it.toBase() })
            val rewrite = mutableListOf<Pair<Int, AudioUi>>()
            newList.forEachIndexed { i, audio ->
                list.forEach { favorite ->
                    if (audio.sameId(favorite))
                        rewrite.add(Pair(i, favorite))
                }
            }
            rewrite.forEach {
                newList[it.first] = it.second
            }
            return Base(id, title, artist, newList)
        }

        override fun open(
            mediaService: MediaService,
            track: AudioUi,
            position: Int,
            orderType: OrderType
        ) {
            mediaService.open(tracks, track, position, orderType)
        }

        override fun updateAdapter(adapter: AlbumAdapter, position: Int) {
            val list = mutableListOf<Any>(this)
            list.addAll(tracks)
            adapter.update(list, position)
        }
    }

    object Empty : AlbumUi() {
        override fun same(item: AlbumUi) = item is Empty
    }
}