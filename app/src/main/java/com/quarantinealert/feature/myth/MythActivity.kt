package com.quarantinealert.feature.myth

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.quarantinealert.R
import kotlinx.android.synthetic.main.activity_myth.*

class MythActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myth)
        setOnClick()
        val value = intent.getStringExtra("value")!!
        if (value.equals("myth")){
            viewHide(precautionslay)
            viewHide(guidelineslay)
            viewHide(aminfectedlay)
            viewHide(some1infectedlay)
            viewHide(homeisolay)
            viewShow(mythlay)
            toolbar.title="Myth Busters"
        } else if (value.equals("precautions")){
            viewHide(mythlay)
            viewHide(guidelineslay)
            viewHide(aminfectedlay)
            viewHide(some1infectedlay)
            viewHide(homeisolay)
            viewShow(precautionslay)
            toolbar.title="Precautions and Preventive Measures"
        } else if (value.equals("guidelines")){
            viewHide(mythlay)
            viewHide(precautionslay)
            viewHide(aminfectedlay)
            viewHide(some1infectedlay)
            viewHide(homeisolay)
            viewShow(guidelineslay)
            toolbar.title="Guidelines for Home Quarantine"
        } else if (value.equals("aminfected")){
            viewHide(mythlay)
            viewHide(guidelineslay)
            viewHide(precautionslay)
            viewHide(some1infectedlay)
            viewHide(homeisolay)
            viewShow(aminfectedlay)
            toolbar.title="Actions"
        } else if (value.equals("knowinfected")){
            viewHide(mythlay)
            viewHide(guidelineslay)
            viewHide(aminfectedlay)
            viewHide(precautionslay)
            viewHide(homeisolay)
            viewShow(some1infectedlay)
            toolbar.title="Actions"
        } else {
            viewHide(mythlay)
            viewHide(guidelineslay)
            viewHide(aminfectedlay)
            viewHide(precautionslay)
            viewShow(homeisolay)
            viewHide(some1infectedlay)
            toolbar.title="Home Isolation"
        }
    }

    private fun setOnClick() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun viewHide(viewHide:View){
        viewHide.visibility=View.GONE
    }

    private fun viewShow(viewShow:View){
        viewShow.visibility=View.VISIBLE
    }
}
