package com.maxim.musicplayer.audioList.presentation

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.maxim.musicplayer.R
import com.maxim.musicplayer.player.media.StartAudio
import java.io.Serializable

abstract class AudioUi: Serializable {
    abstract fun same(item: AudioUi): Boolean
    abstract fun showTitle(textView: TextView)
    open fun showDescription(textView: TextView) = Unit
    open fun showArtist(textView: TextView) = Unit
    open fun showArt(imageView: ImageView) = Unit
    open fun start(startAudio: StartAudio) = Unit
    open fun startAgain(startAudio: StartAudio) = Unit
    open fun showDuration(textView: TextView) = Unit
    open fun setMaxDuration(seekBar: SeekBar) = Unit
    data class Base(
        private val id: Long,
        val title: String,
        private val artist: String,
        private val duration: Long,
        private val album: String,
        private val artBitmap: Bitmap?,
        private var uri: Uri
    ) : AudioUi() {

        override fun same(item: AudioUi) = item is Base && item.id == id
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

        override fun showArt(imageView: ImageView) {
            artBitmap?.let { imageView.setImageBitmap(artBitmap) }
                ?: imageView.setImageResource(R.drawable.baseline_audiotrack_24)
        }

        override fun start(startAudio: StartAudio) {
            startAudio.start(title, artist, uri, artBitmap, false)
        }

        override fun startAgain(startAudio: StartAudio) {
            startAudio.start(title, artist, uri, artBitmap, true)
        }

        override fun showDuration(textView: TextView) {
            textView.text = getTime((duration / 1000).toInt())
        }

        override fun setMaxDuration(seekBar: SeekBar) {
            seekBar.max = duration.toInt()
        }

        private fun getTime(seconds: Int): String {
            val minutes = seconds / 60
            val second = seconds % 60
            return "$minutes:${if (second < 10) "0$second" else second}"
        }
    }

    data class Count(private val size: Int): AudioUi() {
        override fun same(item: AudioUi) = item is Count

        override fun showTitle(textView: TextView) {
            val text = textView.context.getString(R.string.songs, size.toString())
            textView.text = text
        }
    }

    object Space: AudioUi() {
        override fun same(item: AudioUi) = item is Space

        override fun showTitle(textView: TextView) = Unit
    }

    object Empty: AudioUi() {
        override fun same(item: AudioUi) = item is Empty

        override fun showTitle(textView: TextView) = Unit
    }
}