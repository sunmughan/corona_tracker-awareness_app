package com.quarantinealert.feature.stages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.widget.AppCompatTextView
import com.quarantinealert.R
import com.quarantinealert.feature.emergency.EmergencyActivity
import com.quarantinealert.firebase.analytics.AnalyticsUtil
import kotlinx.android.synthetic.main.activity_stages.*
import kotlinx.android.synthetic.main.activity_symptoms.knowMoreButton
import kotlinx.android.synthetic.main.activity_symptoms.toolbar

class StagesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stages)
        setOnClick()

    }

    private fun setOnClick() {
        val pos = intent.getStringExtra("pos")!!
        val view1 = findViewById<AppCompatTextView>(R.id.stage11)
        val view2 = findViewById<AppCompatTextView>(R.id.stage12)
        val view3 = findViewById<AppCompatTextView>(R.id.stage13)
        val view4 = findViewById<AppCompatTextView>(R.id.stage14)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        knowMoreButton.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.SYMPTOMS_EMERGENCY)
            goToEmergencyGuide()
        }
        if(pos.equals("1")){
            focusOnView(view1)
        } else if(pos.equals("2")){
            focusOnView(view2)
        } else if(pos.equals("3")){
            focusOnView(view3)
        } else if(pos.equals("4")){
            focusOnView(view4)
        }

    }

    private fun focusOnView(view: AppCompatTextView) {
        Handler().post(Runnable { scrollView.scrollTo(0, view.getTop()) })
    }

    private fun goToEmergencyGuide() {
        startActivity(Intent(this, EmergencyActivity::class.java))
    }
}
