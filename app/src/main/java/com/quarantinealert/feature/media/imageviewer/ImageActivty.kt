package com.quarantinealert.feature.media.imageviewer

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.quarantinealert.R
import kotlinx.android.synthetic.main.activity_image.*
import kotlinx.android.synthetic.main.activity_symptoms.toolbar


class ImageActivty : AppCompatActivity() {

    lateinit var pager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        setOnClick()
        pager = findViewById(R.id.pager)
        val loginToken:String = intent.getStringExtra("imagepath")!!
       // val pagerpos:String = intent.getStringExtra("pagerpos")!!
     //   pager.currentItem=pagerpos.toInt()

        val stringArray = loginToken.substring(1, loginToken.length-1)
        val arrayList:List<String> =  stringArray.split(",")

        Glide.with(this)
            .load(loginToken).apply(RequestOptions())
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable>,
                    isFirstResource: Boolean): Boolean {
                    bar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean): Boolean {
                    bar.visibility = View.GONE
                    return false
                }
            })
            .into(image)


        Log.e("arraylist",arrayList.toString())
//        val viewPagerAdapter = ImagePagerAdapter(arrayList,this)
//        pager.adapter = viewPagerAdapter
    }

    private fun setOnClick() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
