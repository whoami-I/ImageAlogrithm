package com.mike.imagealogrithm

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mike.imagealogrithm.adapter.HomeListAdapter
import com.mike.imagealogrithm.algorithm.AlgManager
import com.mike.imagealogrithm.base.ColorGenerator
import com.mike.imagealogrithm.base.ItemDataBean
import com.permissionx.guolindev.PermissionX
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    val mDataList = arrayOf(
        ItemDataBean(
            "HSV",
            HSVActivity::class.java, ColorGenerator.getInstance().color
        ),
        ItemDataBean(
            "HSL",
            HSLActivity::class.java, ColorGenerator.getInstance().color
        ), ItemDataBean(
            "Adjust saturation",
            SaturaionActivity::class.java, ColorGenerator.getInstance().color
        ), ItemDataBean(
            "Negative",
            NegativeActivity::class.java, ColorGenerator.getInstance().color
        ), ItemDataBean(
            "GrayScale Log",
            GrayScaleLogActivity::class.java, ColorGenerator.getInstance().color
        ), ItemDataBean(
            "GrayScale Gamma",
            GammaActivity::class.java, ColorGenerator.getInstance().color
        ), ItemDataBean(
            "均值模糊",
            MeanBlurActivity::class.java, ColorGenerator.getInstance().color
        ), ItemDataBean(
            "Robert边缘检测",
            RobertActivity::class.java, ColorGenerator.getInstance().color
        ), ItemDataBean(
            "Prewitt边缘检测",
            PrewittActivity::class.java, ColorGenerator.getInstance().color
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDataList()
        val llm = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv_home.layoutManager = llm
        rv_home.adapter = HomeListAdapter(this, mDataList.asList())
//        val value = AlgManager.hsl2rgb(344f, 1f, 0.57f)
//        Timber.d("${AlgManager.getR(value)},${AlgManager.getB(value)},${AlgManager.getG(value)}")
//        Timber.d(AlgManager.rgb2hsl(255, 36, 96).toString())

        PermissionX.init(this)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                } else {
                    Toast.makeText(
                        this,
                        "These permissions are denied: $deniedList",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }
    }

    fun test() {
        //hsl 344, 1, 0.57
        //rgb 255, 36, 96
    }

    private fun initDataList() {
    }
}
