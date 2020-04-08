package com.quarantinealert.review.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quarantinealert.R
import kotlinx.android.synthetic.main.review_rview.view.*


class ReviewAdapter(
    var headerlist: ArrayList<String>, var contentlist: ArrayList<String>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }


    override fun getItemCount(): Int = headerlist.size

    override fun onBindViewHolder(holder: ReviewAdapter.ViewHolder, position: Int) {
        holder.bind(headerlist[position],contentlist[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.review_rview, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(header: String, content: String) {
            view.header.text = header
            view.content.text = content
        }

    }

    interface OnItemClickListener {
        fun onItemClick(hotline: String)
    }

}