package com.quarantinealert.feature.yoga.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.quarantinealert.R
import kotlinx.android.synthetic.main.yogalay.view.*


class YogaAdapter() : RecyclerView.Adapter<YogaAdapter.ViewHolder>() {

    var catarray: ArrayList<String> = arrayListOf("https://i.ibb.co/xfvCsyB/VID-20200401-WA0031.gif",
        "https://i.ibb.co/w0VSkVt/VID-20200401-WA0030.gif",
        "https://i.ibb.co/mH6yxm0/VID-20200401-WA0029.gif",
        "https://i.ibb.co/28Sd16p/VID-20200401-WA0028.gif",
        "https://i.ibb.co/MMSJ44L/VID-20200401-WA0027.gif",
        "https://i.ibb.co/wQBJ6v2/VID-20200401-WA0026.gif",
        "https://i.ibb.co/3Tx3VJ1/VID-20200401-WA0025.gif",
        "https://i.ibb.co/Qn9GLHJ/VID-20200401-WA0024.gif",
        "https://i.ibb.co/KyfzBpV/VID-20200401-WA0023.gif")

    private var onItemClickListener: OnItemClickListener? = null

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }


    override fun getItemCount(): Int = catarray.size

    override fun onBindViewHolder(holder: YogaAdapter.ViewHolder, position: Int) {
        holder.bind(catarray.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.yogalay, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(hotline: String) {
            Glide.with(itemView.getContext())
                .load(hotline)
                .into(view.videos)
            view.countryName.text = "Yoga "+(adapterPosition+1)
        }

    }

    interface OnItemClickListener {
        fun onItemClick(hotline: String)
    }

}