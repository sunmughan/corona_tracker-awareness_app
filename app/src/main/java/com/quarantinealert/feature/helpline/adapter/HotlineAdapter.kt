package com.quarantinealert.feature.helpline.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quarantinealert.R
import com.quarantinealert.feature.helpline.model.Hotline
import kotlinx.android.synthetic.main.item_hotline.view.*


class HotlineAdapter(var catarray: List<Hotline>?=null) : RecyclerView.Adapter<HotlineAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }


    override fun getItemCount(): Int = catarray!!.size

    override fun onBindViewHolder(holder: HotlineAdapter.ViewHolder, position: Int) {
        holder.bind(catarray!![position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_hotline, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(hotline: Hotline) {
            val hash="#"
            val da = hash+(position+1)
            view.countryName.text = hotline.name
            view.numberText.text = da
            view.confirmCountText.text = hotline.phoneNumber
            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(hotline.phoneNumber)
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(hotline: String)
    }

}