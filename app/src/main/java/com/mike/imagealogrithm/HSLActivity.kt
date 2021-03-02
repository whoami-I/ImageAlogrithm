package com.mike.imagealogrithm

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.mike.imagealogrithm.algorithm.AlgManager
import com.mike.imagealogrithm.base.OnSeekBarChangeListener
import com.mike.imagealogrithm.base.setMyOnSeekBarChangeListener
import kotlinx.android.synthetic.main.activity_hsl.*

class HSLActivity : AppCompatActivity() {
    var createBitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hsl)
        title = "HSL to RGB"
        val makeColor = {
            var rgb =
                AlgManager.hsl2rgb(
                    seekbar_h.progress.toFloat(),
                    seekbar_s.progress.toFloat() / 100.0F, seekbar_l.progress.toFloat() / 100.0F
                )
//            rgb=AlgManager.makeColor(0xFF,0xFF,0)
            rgb = rgb or (0xFF shl 24)
            createBitmap?.eraseColor(rgb)
        }

        seekbar_h.min = 0
        seekbar_h.max = 360
        seekbar_h.setMyOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_h.text = seekbar_h.progress.toString()
                makeColor()
            }
        })

        seekbar_s.min = 0
        seekbar_s.max = 100
        seekbar_s.setMyOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_s.text = (seekbar_s.progress / 100.0).toString()
                makeColor()
            }
        })

        seekbar_l.min = 0
        seekbar_l.max = 100
        seekbar_l.setMyOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_l.text = (seekbar_l.progress / 100.0f).toString()
                makeColor()
            }
        })

        window.decorView.post {
            createBitmap =
                Bitmap.createBitmap(img_preview.width, img_preview.height, Bitmap.Config.ARGB_8888)
            img_preview.setImageBitmap(createBitmap)
            makeColor()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        img_preview.setImageBitmap(null)
        createBitmap?.recycle()
    }
}