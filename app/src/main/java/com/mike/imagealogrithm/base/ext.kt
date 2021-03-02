package com.mike.imagealogrithm.base

import android.widget.ProgressBar
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar

fun AppCompatSeekBar.setMyOnSeekBarChangeListener(l: OnSeekBarChangeListener) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            l.onProgressChanged(seekBar, progress, fromUser)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    })
    // trigger once
    l.onProgressChanged(this, progress, false)
}

interface OnSeekBarChangeListener {

    fun onProgressChanged(
        seekBar: SeekBar?,
        progress: Int,
        fromUser: Boolean
    )
}