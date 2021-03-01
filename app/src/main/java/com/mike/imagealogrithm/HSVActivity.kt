package com.mike.imagealogrithm

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.mike.imagealogrithm.algorithm.AlgManager
import kotlinx.android.synthetic.main.activity_hsv.*

class HSVActivity : AppCompatActivity() {
    var createBitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hsv)
        title = "HSV to RGB"
        val makeColor = {
            var rgb =
                AlgManager.hsv2rgb(
                    seekbar_h.progress.toFloat(),
                    seekbar_s.progress.toFloat() / 100.0F, seekbar_v.progress.toFloat()
                )
//            rgb=AlgManager.makeColor(0xFF,0xFF,0)
            rgb = rgb or (0xFF shl 24)
            createBitmap?.eraseColor(rgb)
        }

        seekbar_h.min = 0
        seekbar_h.max = 360
        seekbar_h.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_h.text = seekbar_h.progress.toString()
                makeColor()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        seekbar_s.min = 0
        seekbar_s.max = 100
        seekbar_s.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_s.text = (seekbar_s.progress / 100.0).toString()
                makeColor()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        seekbar_v.min = 0
        seekbar_v.max = 255
        seekbar_v.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_v.text = seekbar_v.progress.toString()
                makeColor()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        window.decorView.post {
            createBitmap =
                Bitmap.createBitmap(img_preview.width, img_preview.height, Bitmap.Config.ARGB_8888)
            img_preview.setImageBitmap(createBitmap)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        img_preview.setImageBitmap(null)
        createBitmap?.recycle()
    }
}