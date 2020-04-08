package com.quarantinealert.feature.media.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.quarantinealert.R
import com.quarantinealert.feature.media.imageviewer.AllImages
import com.quarantinealert.feature.media.imageviewer.MyGovList

class Images : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root =  inflater.inflate(R.layout.fragment_images, container, false)

        val mygov:MaterialCardView = root.findViewById(R.id.mygov)
        val social:MaterialCardView = root.findViewById(R.id.social)
        val volunteers:MaterialCardView = root.findViewById(R.id.volunteers)


        mygov.setOnClickListener( View.OnClickListener {
            val intent = Intent(activity, MyGovList::class.java)
            startActivity(intent)
        })
        social.setOnClickListener( View.OnClickListener {
            val intent = Intent(activity, AllImages::class.java)
            intent.putExtra("value","social")
            startActivity(intent)
        })
        volunteers.setOnClickListener( View.OnClickListener {
            val intent = Intent(activity, AllImages::class.java)
            intent.putExtra("value","volunteers")
            startActivity(intent)
        })

        return root
    }
    companion object {
        fun newInstance(): Images = Images()
    }
}