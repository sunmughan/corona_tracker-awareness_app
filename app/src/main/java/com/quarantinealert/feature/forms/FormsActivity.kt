package com.quarantinealert.feature.forms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.quarantinealert.R
import com.quarantinealert.feature.forms.fragments.Contribute
import com.quarantinealert.feature.forms.fragments.SelfDiagnosis
import com.quarantinealert.feature.media.adapter.ViewPagerAdapter
import com.quarantinealert.feature.media.fragments.Volunteer
import kotlinx.android.synthetic.main.activity_symptoms.*

class FormsActivity : AppCompatActivity() {

    lateinit var conFrag: Fragment
    lateinit var volunFrag: Fragment
    lateinit var selfFrag: Fragment
    lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forms)
        viewPager = findViewById(R.id.pager)
        setUpPager()
        setOnClick()
    }

    private fun setOnClick() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setUpPager(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        conFrag = Contribute.newInstance()
        volunFrag = Volunteer.newInstance()
        selfFrag = SelfDiagnosis.newInstance()
        adapter.addFragment(selfFrag)
        adapter.addFragment(volunFrag)
        adapter.addFragment(conFrag)
        viewPager.setAdapter(adapter)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if(position==0){
                    toolbar.title = "Self Diagnosis (Swipe right)"
                } else if(position==1){
                    toolbar.title = "Volunteer (Swipe right or left)"
                } else {
                    toolbar.title = "Contribute (Swipe left)"
                }
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

}
