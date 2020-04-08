package com.quarantinealert.feature.media.imageviewer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.quarantinealert.R
import kotlinx.android.synthetic.main.activity_my_gov_list.*
import kotlinx.android.synthetic.main.activity_symptoms.toolbar

class MyGovList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_gov_list)
        setOnClick()
    }
    private fun setOnClick() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        tips.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AllImages::class.java)
            intent.putExtra("value","tips")
            startActivity(intent)
        })
        updates.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AllImages::class.java)
            intent.putExtra("value","updates")
            startActivity(intent)
        })

    }
}
