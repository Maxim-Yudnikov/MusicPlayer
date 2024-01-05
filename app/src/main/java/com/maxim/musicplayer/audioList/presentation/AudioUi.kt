package com.maxim.musicplayer.audioList.presentation

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.maxim.musicplayer.R
import com.maxim.musicplayer.player.media.StartAudio

interface AudioUi {
    fun same(item: AudioUi): Boolean
    fun showTitle(textView: TextView)
    fun showDescription(textView: TextView)
    fun showArtist(textView: TextView)
    fun showArt(imageView: ImageView)
    fun start(startAudio: StartAudio)
    fun startAgain(startAudio: StartAudio)
    fun showDuration(textView: TextView)
    fun setMaxDuration(seekBar: SeekBar)
    data class Base(
        private val id: Long,
        private val title: String,
        private val artist: String,
        private val duration: Int,
        private val album: String,
        private val artBitmap: Bitmap?,
        private val uri: Uri
    ) : AudioUi {
        override fun same(item: AudioUi) = item is Base && item.id == id
        override fun showTitle(textView: TextView) {
            textView.text = title
        }

        override fun showDescription(textView: TextView) {
            val text = "$artist - ${getTime(duration)}"
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
            startAudio.start(title, artist, uri)
        }

        override fun startAgain(startAudio: StartAudio) {
            startAudio.start(title, artist, uri, true)
        }

        override fun showDuration(textView: TextView) {
            textView.text = getTime(duration)
        }

        override fun setMaxDuration(seekBar: SeekBar) {
            seekBar.max = duration * 1000
        }

        private fun getTime(seconds: Int): String {
            val minutes = seconds / 60
            val second = seconds % 60
            return "$minutes:${if (second < 10) "0$second" else second}"
        }
    }
}