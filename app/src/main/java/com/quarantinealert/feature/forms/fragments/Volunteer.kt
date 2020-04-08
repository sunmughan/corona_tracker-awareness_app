package com.quarantinealert.feature.media.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.quarantinealert.R
import com.quarantinealert.review.ReviewActivity
import kotlinx.android.synthetic.main.fragment_volunteer.*
import kotlin.collections.ArrayList

class Volunteer : Fragment() {

    var radioValue:String=""
    var tableString:String=""
    lateinit var radioGroup:RadioGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root =  inflater.inflate(R.layout.fragment_volunteer, container, false)
        val sendButton: MaterialButton = root.findViewById(R.id.sendButton)
        radioGroup = root.findViewById(R.id.radioGroup)
        sendButton.setOnClickListener(View.OnClickListener {
            if (edtnamedisplay.text.toString().isEmpty()) {
                Toast.makeText(activity, "Please enter name !", Toast.LENGTH_SHORT).show()
            } else if(edtmaildisplay.text.toString().isEmpty()){
                Toast.makeText(activity, "Please enter email !", Toast.LENGTH_SHORT).show()
            } else if(edtnumdisplay.text.toString().isEmpty()){
                Toast.makeText(activity, "Please enter number !", Toast.LENGTH_SHORT).show()
            } else if(edtdevpdisplay.text.toString().isEmpty()){
                Toast.makeText(activity, "Please enter a link !", Toast.LENGTH_SHORT).show()
            } else if(edtlocdisplay.text.toString().isEmpty()){
                Toast.makeText(activity, "Please enter location !", Toast.LENGTH_SHORT).show()
            } else if(radioValue.isEmpty()){
                Toast.makeText(activity, "Please choose preference for contribution !", Toast.LENGTH_SHORT).show()
            } else {
                val headersList: ArrayList<String> = arrayListOf("Name", "Email", "Mobile", "Profile Link", "Preference for contribution")
                val rowsList: ArrayList<String> = arrayListOf(edtnamedisplay.text.toString(), edtmaildisplay.text.toString(), edtnumdisplay.text.toString(), edtdevpdisplay.text.toString(), radioValue)
                val intent = Intent(activity, ReviewActivity::class.java)
                intent.putExtra("headerlist", headersList)
                intent.putExtra("contentlist", rowsList)
                intent.putExtra("title", "Volunteer")
                startActivity(intent)
            }
        })

        radioGroup.setOnCheckedChangeListener(checkedChangeListener)
        return root
    }

    private val checkedChangeListener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
        val rb = group.findViewById<View>(checkedId) as RadioButton
        if (null != rb && checkedId > -1) {
            radioValue = rb.text.toString()
        }
    }

    companion object {
        fun newInstance(): Volunteer = Volunteer()
    }
}