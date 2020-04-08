package com.quarantinealert.feature.yoga

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.quarantinealert.R
import com.quarantinealert.feature.yoga.adapter.YogaAdapter
import kotlinx.android.synthetic.main.activity_emergency.toolbar
import kotlinx.android.synthetic.main.activity_yogo.*

class YogaActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yogo)
        setOnClick()
        videos_rview.layoutManager = LinearLayoutManager(this)
        videos_rview.itemAnimator = DefaultItemAnimator()
        val yogaAdapter = YogaAdapter()
        videos_rview.adapter = yogaAdapter
    }

    private fun setOnClick() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
