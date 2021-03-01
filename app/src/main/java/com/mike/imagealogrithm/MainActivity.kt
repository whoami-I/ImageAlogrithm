package com.mike.imagealogrithm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mike.imagealogrithm.adapter.HomeListAdapter
import com.mike.imagealogrithm.base.ColorGenerator
import com.mike.imagealogrithm.base.ItemDataBean
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var mDataList = ArrayList<ItemDataBean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDataList()
        val llm = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv_home.layoutManager = llm
        rv_home.adapter = HomeListAdapter(this, mDataList)
    }

    private fun initDataList() {
        mDataList.add(
            ItemDataBean(
                "HSV",
                HSVActivity::class.java, ColorGenerator.getInstance().color
            )
        )
    }
}
