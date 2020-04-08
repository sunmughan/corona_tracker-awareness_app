package com.quarantinealert.feature.media.adapter

import android.widget.RelativeLayout
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.annotation.NonNull
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.quarantinealert.R
import com.quarantinealert.feature.media.imageviewer.ImageActivty
import com.quarantinealert.util.TouchImageView


class ImagePagerAdapter(private val _activity: List<String>, val imageActivty1: ImageActivty) : PagerAdapter() {
    private var inflater: LayoutInflater? = null

    override fun getCount(): Int {
        return this._activity.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    @NonNull
    override fun instantiateItem(@NonNull container: ViewGroup, position: Int): Any {
        val imgDisplay: TouchImageView
        val bar: ProgressBar
        val btnClose: Button
        inflater = imageActivty1.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewLayout = inflater!!.inflate(
            R.layout.imageviewpager, container,
            false
        )
        imgDisplay = viewLayout.findViewById(R.id.image)
        bar = viewLayout.findViewById(R.id.bar)
        Log.e("image     ","                 "+_activity.get(position))
        Glide.with(imageActivty1).load(_activity.get(position))
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
            .into(imgDisplay)
        container.addView(viewLayout)
        return viewLayout
    }

    override fun destroyItem(@NonNull container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)

    }
}