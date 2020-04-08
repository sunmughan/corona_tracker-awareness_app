package com.quarantinealert.feature.media.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.quarantinealert.R
import com.quarantinealert.model.UriModel

class PhotosAdapter(var catarray: MutableList<UriModel>? = null) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    var rowindex:Int = -1
    private var onItemClickListener: ItemClickListener? = null
    private val context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.photos_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uriModel:UriModel = catarray!!.get(position)
        Glide.with(holder.imageView.context)
            .load(uriModel.uri).apply(RequestOptions())
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable>,
                    isFirstResource: Boolean): Boolean {
                    holder.bar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean): Boolean {
                    holder.bar.visibility = View.GONE
                    return false
                }
            })
            .into(holder.imageView)
        holder.imageView.setOnClickListener {
            onItemClickListener?.onItemClick( holder.imageView, position,uriModel.uri)
        }
    }

    override fun getItemCount(): Int {
        return catarray!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setItemClickListener(clickListener: ItemClickListener) {
        onItemClickListener = clickListener
    }


    interface ItemClickListener {
        fun onItemClick(
            view: View,
            position: Int,
            postId: String
        )
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        var bar: ProgressBar = itemView.findViewById(R.id.bar)
    }
}