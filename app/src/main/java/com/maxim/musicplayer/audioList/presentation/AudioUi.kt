package com.maxim.musicplayer.audioList.presentation

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import com.maxim.musicplayer.R

abstract class AudioUi {
    abstract fun same(item: AudioUi): Boolean
    abstract fun showTitle(textView: TextView)
    abstract fun showDescription(textView: TextView)
    abstract fun showArtist(textView: TextView)
    abstract fun showArt(imageView: ImageView)
    data class Base(
        private val id: Long,
        private val title: String,
        private val artist: String,
        private val duration: Int,
        private val album: String,
        private val artBitmap: Bitmap?,
        private val uri: Uri
    ) : AudioUi() {
        override fun same(item: AudioUi) = item is Base && item.id == id
        override fun showTitle(textView: TextView) {
            textView.text = title
        }

        override fun showDescription(textView: TextView) {
            val minutes = duration / 60
            val second = duration % 60
            val timeUi = "$minutes:${if (second < 10) "0$second" else second}"
            val s = "$artist - $timeUi"
            textView.text = s
        }

        override fun showArtist(textView: TextView) {
            textView.text = artist
        }

        override fun showArt(imageView: ImageView) {
            artBitmap?.let { imageView.setImageBitmap(artBitmap) }
                ?: imageView.setImageResource(R.drawable.baseline_audiotrack_24)
        }
    }
}