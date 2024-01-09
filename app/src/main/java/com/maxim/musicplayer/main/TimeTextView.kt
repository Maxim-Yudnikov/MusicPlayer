package com.maxim.musicplayer.main

import android.content.Context
import android.util.AttributeSet
import android.util.Log

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
        val minutes = seconds / 60
        val second = seconds % 60
        val timeUi = "$minutes:${if (second < 10) "0$second" else second}"
        text = timeUi
    }

    fun showStringAndTime(string: String, seconds: Int) {
        Log.d("MyLog", "total: $seconds")
        val minutes = seconds / 60
        val second = seconds % 60
        val timeUi = "$string$minutes:${if (second < 10) "0$second" else second}"
        text = timeUi
    }
}