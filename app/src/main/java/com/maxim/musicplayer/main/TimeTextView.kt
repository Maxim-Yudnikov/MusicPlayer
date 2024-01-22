package com.maxim.musicplayer.main

import android.content.Context
import android.util.AttributeSet

class TimeTextView : androidx.appcompat.widget.AppCompatTextView {
    //region constructors
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    //endregion

    fun showTime(seconds: Int) {
        text = time(seconds)
    }

    fun showStringAndTime(string: String, seconds: Int) {
        val timeUi = "$string${time(seconds)}"
        text = timeUi
    }

    private fun time(s: Int): String {
        val hours = s / 3600
        val minutes = s % 3600 / 60
        val seconds = s % 60
        return (if (hours > 0) "$hours:" else "") +
                (if (minutes < 10) "0$minutes" else minutes) + ":" +
                (if (seconds < 10) "0$seconds" else seconds)
    }
}