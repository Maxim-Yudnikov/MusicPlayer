package com.maxim.musicplayer.audioList.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.util.AttributeSet
import com.maxim.musicplayer.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ArtImageView : androidx.appcompat.widget.AppCompatImageView {
    //region constructors
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    //endregion

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private fun showBitmap(bitmap: Bitmap?) {
        bitmap?.let { setImageBitmap(it) }
            ?: setImageResource(R.drawable.baseline_audiotrack_24)
    }

    private var actualUri: Uri = Uri.EMPTY

    fun notifyArtChanged() {
        actualUri = Uri.EMPTY
    }

    fun setArt(uri: Uri, fullQuality: Boolean) {
        actualUri = uri
        if (fullQuality) {
            try {
                val parseFileDescriptor =
                    context.contentResolver.openFileDescriptor(uri, "r")
                val fileDescriptor = parseFileDescriptor!!.fileDescriptor
                setImageBitmap(BitmapFactory.decodeFileDescriptor(fileDescriptor))
                parseFileDescriptor.close()
                return
            } catch (_: Exception) {
                setImageResource(R.drawable.baseline_audiotrack_24)
            }
        }

        if (!bitmapMap.containsKey(uri)) {
            coroutineScope.launch(Dispatchers.Default) {
                try {
                    val parseFileDescriptor =
                        context.contentResolver.openFileDescriptor(uri, "r")
                    val fileDescriptor = parseFileDescriptor!!.fileDescriptor

                    val options = BitmapFactory.Options().apply {
                        inJustDecodeBounds = true
                    }
                    BitmapFactory.decodeFileDescriptor(fileDescriptor, Rect(), options)
                    options.apply {
                        inSampleSize = calculateInSampleSize(this, 128, 128)
                        inJustDecodeBounds = false
                    }

                    bitmapMap[uri] =
                        BitmapFactory.decodeFileDescriptor(fileDescriptor, Rect(), options)

                    parseFileDescriptor.close()

                } catch (_: Exception) {
                    bitmapMap[uri] = null
                }
                if (actualUri == uri) {
                    withContext(Dispatchers.Main) {
                        showBitmap(bitmapMap[uri])
                    }
                }
            }
        } else {
            showBitmap(bitmapMap[uri])
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    companion object {
        private val bitmapMap = mutableMapOf<Uri, Bitmap?>()
    }
}