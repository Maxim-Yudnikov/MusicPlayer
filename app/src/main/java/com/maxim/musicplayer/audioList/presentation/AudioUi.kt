package com.maxim.musicplayer.audioList.presentation

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.maxim.musicplayer.R
import com.maxim.musicplayer.favoriteList.data.FavoritesActions
import com.maxim.musicplayer.main.TimeTextView
import com.maxim.musicplayer.order.presentation.OrderAdapter
import com.maxim.musicplayer.player.media.StartAudio
import java.io.Serializable

abstract class AudioUi : Serializable {
    abstract fun same(item: AudioUi): Boolean
    open fun sameId(item: AudioUi): Boolean = false
    abstract fun showTitle(textView: TextView)
    open fun showDescription(textView: TimeTextView) = Unit
    open fun showArtist(textView: TextView) = Unit
    open fun showArt(artImageView: ArtImageView, fullQuality: Boolean) = Unit
    open fun start(startAudio: StartAudio, contentResolver: ContentResolver) = Unit
    open fun startAgain(startAudio: StartAudio, contentResolver: ContentResolver) = Unit
    open fun showDuration(textView: TimeTextView) = Unit
    open fun setMaxDuration(seekBar: SeekBar) = Unit
    open fun showFavorite(imageView: ImageView) = Unit
    open suspend fun changeFavorite(favoritesActions: FavoritesActions) = Unit
    open fun changeFavorite(): AudioUi = Empty
    open fun toBase(): AudioUi = Empty
    open fun removeListener(listener: OrderAdapter.Listener) = Unit

    open fun duration(): Long = 0

    abstract class Abstract(
        val id: Long,
        private val title: String,
        private val artist: String,
        private val duration: Long,
        private val album: String,
        private val artUri: Uri,
        private var uri: Uri,
    ) : AudioUi() {
        override fun sameId(item: AudioUi) = item is Abstract && item.id == id

        override fun showTitle(textView: TextView) {
            textView.text = title
        }

        override fun showDescription(textView: TimeTextView) {
            textView.showStringAndTime("$artist - ", (duration / 1000).toInt())
        }

        override fun showArtist(textView: TextView) {
            textView.text = artist
        }

        override fun showArt(artImageView: ArtImageView, fullQuality: Boolean) {
            artImageView.setArt(artUri, fullQuality)
        }

        override fun start(startAudio: StartAudio, contentResolver: ContentResolver) {
            startAudio.start(title, artist, uri, getBitmap(artUri, contentResolver), false)
        }

        override fun startAgain(startAudio: StartAudio, contentResolver: ContentResolver) {
            startAudio.start(title, artist, uri, getBitmap(artUri, contentResolver), true)
        }

        override fun showDuration(textView: TimeTextView) {
            textView.showTime((duration / 1000).toInt())
        }

        override fun setMaxDuration(seekBar: SeekBar) {
            seekBar.max = duration.toInt()
        }

        private fun getBitmap(uri: Uri, contentResolver: ContentResolver): Bitmap? {
            return try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
                else
                    MediaStore.Images.Media.getBitmap(contentResolver, uri)
            } catch (_: Exception) {
                null
            }
        }

        override fun removeListener(listener: OrderAdapter.Listener) {
            listener.remove(id)
        }

        override fun duration() = duration
    }

    data class Base(
        private val baseId: Long,
        private val title: String,
        private val artist: String,
        private val duration: Long,
        private val album: String,
        private val artUri: Uri,
        private var uri: Uri
    ) : Abstract(baseId, title, artist, duration, album, artUri, uri) {

        override fun same(item: AudioUi) = item is Base && item.baseId == baseId

        override fun showFavorite(imageView: ImageView) {
            imageView.setImageResource(R.drawable.favorite_border_24)
        }

        override suspend fun changeFavorite(favoritesActions: FavoritesActions) {
            favoritesActions.addToFavorite(baseId, title, artist, duration, album, artUri, uri)
        }

        override fun changeFavorite() =
            Favorite(baseId, title, artist, duration, album, artUri, uri)

        override fun toBase() = this
    }

    data class Favorite(
        private val favoriteId: Long,
        private val title: String,
        private val artist: String,
        private val duration: Long,
        private val album: String,
        private val artUri: Uri,
        private var uri: Uri
    ) : Abstract(favoriteId, title, artist, duration, album, artUri, uri) {

        override fun same(item: AudioUi) = item is Favorite && item.favoriteId == favoriteId

        override fun showFavorite(imageView: ImageView) {
            imageView.setImageResource(R.drawable.favorite_24)
        }

        override suspend fun changeFavorite(favoritesActions: FavoritesActions) {
            favoritesActions.removeFromFavorites(favoriteId)
        }

        override fun changeFavorite() =
            Base(favoriteId, title, artist, duration, album, artUri, uri)

        override fun toBase() = Base(favoriteId, title, artist, duration, album, artUri, uri)
    }

    data class OrderTitle(
        private val actualPosition: Int,
        private val count: Int,
        private val totalDuration: Int
    ) : AudioUi() {
        override fun same(item: AudioUi) = item is OrderTitle
        override fun showTitle(textView: TextView) = Unit

        override fun showDescription(textView: TimeTextView) {
            textView.showStringAndTime("$actualPosition/$count - ", totalDuration / 1000)
        }
    }

    data class Count(private val size: Int) : AudioUi() {
        override fun same(item: AudioUi) = item is Count

        override fun showTitle(textView: TextView) {
            val text = textView.context.getString(R.string.tracks, size.toString())
            textView.text = text
        }
    }

    object Empty : AudioUi() {
        override fun same(item: AudioUi) = item is Empty

        override fun showTitle(textView: TextView) = Unit
    }
}