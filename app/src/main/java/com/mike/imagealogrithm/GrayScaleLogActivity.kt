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


class GrayScaleLogActivity : AppCompatActivity() {
    var srcBitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adjust_negative)
        title = "GrayScaleLog"
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
                val futureBitmap = Glide.with(this@GrayScaleLogActivity)
                    .asBitmap()
                    .load(it)
                    .submit()
                srcBitmap = futureBitmap.get()
                AlgManager.grayScaleLog(srcBitmap!!)
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
    }
}