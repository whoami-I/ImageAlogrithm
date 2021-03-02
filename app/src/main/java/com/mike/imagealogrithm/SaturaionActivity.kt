package com.mike.imagealogrithm

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.mike.imagealogrithm.algorithm.AlgManager
import kotlinx.android.synthetic.main.activity_adjust_saturation.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


class SaturaionActivity : AppCompatActivity() {
    var srcBitmap: Bitmap? = null
    var tmpBitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adjust_saturation)
        title = "Adjust saturation by HSL"

        seekbar_s.min = -100
        seekbar_s.max = 100
        seekbar_s.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_s.text = (seekbar_s.progress / 100.0).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val v = seekBar!!.progress / 100f
                val progressDialog = ProgressDialog(this@SaturaionActivity)
                progressDialog.show()
                progressDialog.setCancelable(false)
                lifecycleScope.launch(Dispatchers.IO) {
                    val canvas = Canvas(tmpBitmap!!)
                    canvas.drawBitmap(srcBitmap!!, 0.0f, 0.0f, null)
                    AlgManager.addSaturation_n(v, tmpBitmap!!)
                    withContext(Dispatchers.Main.immediate) {
                        img_preview.setImageBitmap(tmpBitmap)
                        Timber.d("complete process image")
                        progressDialog.dismiss()
                    }
                }
            }
        })
        img_preview.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uri = data!!.data
        uri?.also {
            lifecycleScope.launch(Dispatchers.IO) {
                val futureBitmap = Glide.with(this@SaturaionActivity)
                    .asBitmap()
                    .load(it)
                    .submit()
                srcBitmap = futureBitmap.get()
                tmpBitmap = Bitmap.createBitmap(srcBitmap!!)
                withContext(Dispatchers.Main.immediate) {
                    img_preview.setImageBitmap(srcBitmap)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        img_preview.setImageBitmap(null)
        srcBitmap?.recycle()
        tmpBitmap?.recycle()
    }
}