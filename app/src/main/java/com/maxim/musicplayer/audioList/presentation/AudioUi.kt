package com.maxim.musicplayer.audioList.presentation

import android.net.Uri
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.maxim.musicplayer.R
import com.maxim.musicplayer.favoriteList.data.FavoritesActions
import com.maxim.musicplayer.player.media.StartAudio
import java.io.Serializable

abstract class AudioUi : Serializable {
    abstract fun same(item: AudioUi): Boolean
    abstract fun showTitle(textView: TextView)
    open fun showDescription(textView: TextView) = Unit
    open fun showArtist(textView: TextView) = Unit
    open fun showArt(artImageView: ArtImageView, fullQuality: Boolean) = Unit
    open fun start(startAudio: StartAudio) = Unit
    open fun startAgain(startAudio: StartAudio) = Unit
    open fun showDuration(textView: TextView) = Unit
    open fun setMaxDuration(seekBar: SeekBar) = Unit
    open fun showFavorite(imageView: ImageView) = Unit
    open suspend fun changeFavorite(favoritesActions: FavoritesActions) = Unit
    open fun changeFavorite(): AudioUi = Empty

    abstract class Abstract(
        private val id: Long,
        private val title: String,
        private val artist: String,
        private val duration: Long,
        private val album: String,
        private val artUri: Uri,
        private var uri: Uri
    ) : AudioUi() {
        override fun same(item: AudioUi) = item is Abstract && item.id == id
        override fun showTitle(textView: TextView) {
            textView.text = title
        }

        override fun showDescription(textView: TextView) {
            val text = "$artist - ${getTime((duration / 1000).toInt())}"
            textView.text = text
        }

        override fun showArtist(textView: TextView) {
            textView.text = artist
        }

        override fun showArt(artImageView: ArtImageView, fullQuality: Boolean) {
            artImageView.setArt(artUri, fullQuality)
        }

        override fun start(startAudio: StartAudio) {
            startAudio.start(title, artist, uri, null, false)
        }

        override fun startAgain(startAudio: StartAudio) {
            startAudio.start(title, artist, uri, null, true)
        }

        override fun showDuration(textView: TextView) {
            textView.text = getTime((duration / 1000).toInt())
        }

        override fun setMaxDuration(seekBar: SeekBar) {
            seekBar.max = duration.toInt()
        }

        //todo custom view timeTextView
        private fun getTime(seconds: Int): String {
            val minutes = seconds / 60
            val second = seconds % 60
            return "$minutes:${if (second < 10) "0$second" else second}"
        }
    }

    data class Base(
        private val id: Long,
        private val title: String,
        private val artist: String,
        private val duration: Long,
        private val album: String,
        private val artUri: Uri,
        private var uri: Uri
    ) : Abstract(id, title, artist, duration, album, artUri, uri) {

        override fun showFavorite(imageView: ImageView) {
            imageView.setImageResource(R.drawable.favorite_24)
        }

        override suspend fun changeFavorite(favoritesActions: FavoritesActions) {
            favoritesActions.addToFavorite(id, title, artist, duration, album, artUri, uri)
        }

        override fun changeFavorite() = Favorite(id, title, artist, duration, album, artUri, uri)
    }

    data class Favorite(
        private val id: Long,
        private val title: String,
        private val artist: String,
        private val duration: Long,
        private val album: String,
        private val artUri: Uri,
        private var uri: Uri
    ) : Abstract(id, title, artist, duration, album, artUri, uri) {

        override fun showFavorite(imageView: ImageView) {
            imageView.setImageResource(R.drawable.favorite_full_24)
        }

        override suspend fun changeFavorite(favoritesActions: FavoritesActions) {
            favoritesActions.removeFromFavorites(id)
        }

        override fun changeFavorite() = Base(id, title, artist, duration, album, artUri, uri)
    }

    data class Count(private val size: Int) : AudioUi() {
        override fun same(item: AudioUi) = item is Count

        override fun showTitle(textView: TextView) {
            val text = textView.context.getString(R.string.songs, size.toString())
            textView.text = text
        }
    }

    object Space : AudioUi() {
        override fun same(item: AudioUi) = item is Space

        override fun showTitle(textView: TextView) = Unit
    }

    object Empty : AudioUi() {
        override fun same(item: AudioUi) = item is Empty

        override fun showTitle(textView: TextView) = Unit
    }
}