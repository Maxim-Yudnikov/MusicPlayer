package com.maxim.musicplayer.audioList.presentation

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import com.anggrayudi.storage.media.MediaFile
import com.maxim.musicplayer.R

abstract class AudioUi {
    abstract fun same(item: AudioUi): Boolean
    abstract fun showTitle(textView: TextView)
    abstract fun showDescription(textView: TextView)
    abstract fun showArt(imageView: ImageView)
    data class Base(
        private val id: Long,
        private val title: String,
        private val artist: String,
        private val duration: Int,
        private val album: String,
        private val art: Uri,
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

        override fun showArt(imageView: ImageView) {
            val file = MediaFile(imageView.context, uri)
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(file.absolutePath)

            if (retriever.embeddedPicture != null)
                imageView.setImageURI(art)
            else
                imageView.setImageResource(R.drawable.baseline_audiotrack_24)
        }
    }
}