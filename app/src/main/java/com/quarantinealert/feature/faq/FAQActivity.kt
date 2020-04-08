package com.quarantinealert.feature.myth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.quarantinealert.R
import kotlinx.android.synthetic.main.activity_faq.*

class FAQActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)
        setOnClick()
    }

    private fun setOnClick() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

}
