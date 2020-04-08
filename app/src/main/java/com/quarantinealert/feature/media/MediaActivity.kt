package com.quarantinealert.feature.media

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.quarantinealert.R
import com.quarantinealert.feature.media.adapter.ViewPagerAdapter
import com.quarantinealert.feature.media.fragments.Documents
import com.quarantinealert.feature.media.fragments.Images
import com.quarantinealert.feature.media.fragments.Videos
import kotlinx.android.synthetic.main.activity_symptoms.*

class MediaActivity : AppCompatActivity() {

    lateinit var imagesFrag:Fragment
    lateinit var videosFrag:Fragment
    lateinit var documentsFrag:Fragment
    lateinit var viewPager: ViewPager
    var prevMenuItem: MenuItem? = null
    lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        setOnClick()
        viewPager = findViewById(R.id.pager)
        bottomNavigation = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        setUpPager()
    }

    private fun setOnClick() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setUpPager(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        imagesFrag = Images.newInstance()
        videosFrag = Videos.newInstance()
        documentsFrag = Documents.newInstance()
        adapter.addFragment(imagesFrag)
        adapter.addFragment(documentsFrag)
        adapter.addFragment(videosFrag)
        viewPager.setAdapter(adapter)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if(position==0){
                    toolbar.title = "Photos"
                } else if(position==1){
                    toolbar.title = "Documents"
                } else {
                    toolbar.title = "Videos"
                }
            }

            override fun onPageSelected(position: Int) {
                if (prevMenuItem != null) prevMenuItem?.setChecked(false) else bottomNavigation.getMenu().getItem(
                    0
                ).setChecked(false)
                bottomNavigation.getMenu().getItem(position).setChecked(true)
                prevMenuItem = bottomNavigation.getMenu().getItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_images -> {
               // toolbar.title = "Photos"
                viewPager.setCurrentItem(0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_documents -> {
             //   toolbar.title = "Documents"
                viewPager.setCurrentItem(1)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_videos -> {
                //  toolbar.title = "Videos"
                viewPager.setCurrentItem(2)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

}
