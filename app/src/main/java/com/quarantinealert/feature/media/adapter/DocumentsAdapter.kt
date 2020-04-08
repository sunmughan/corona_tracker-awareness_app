package com.quarantinealert.feature.media.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quarantinealert.R
import com.quarantinealert.model.PdfModel
import kotlinx.android.synthetic.main.layout_documents.view.*


class DocumentsAdapter(var catarray: MutableList<PdfModel>? = null) : RecyclerView.Adapter<DocumentsAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }


    override fun getItemCount(): Int = catarray!!.size

    override fun onBindViewHolder(holder: DocumentsAdapter.ViewHolder, position: Int) {
        holder.bind(catarray!![position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_documents, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(hotline: PdfModel) {
            val hash="#"
            val da = hash+(position+1)
            view.tvname1.text = hotline.fileName
            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(hotline.url,hotline.fileName)
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(hotline: String, get: String)
    }

}