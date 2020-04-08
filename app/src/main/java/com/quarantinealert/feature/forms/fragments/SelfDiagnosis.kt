package com.quarantinealert.feature.forms.fragments

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
import kotlinx.android.synthetic.main.fragment_selfdiagnosis.*
import kotlin.collections.ArrayList


class SelfDiagnosis : Fragment() {

    var howyoufeel:String=""
    var havefever:String=""
    var cough:String=""
    var fatigue:String=""
    var breathe:String=""
    var wherear:String=""
    var tableString:String=""
    lateinit var fevelradioGroup: RadioGroup
    lateinit var feelradioGroup: RadioGroup
    lateinit var coughradioGroup: RadioGroup
    lateinit var fatigueradioGroup: RadioGroup
    lateinit var breatheadioGroup: RadioGroup
    lateinit var whereareadioGroup: RadioGroup


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root =  inflater.inflate(R.layout.fragment_selfdiagnosis, container, false)
        val sendButton: MaterialButton = root.findViewById(R.id.sendButton)
        feelradioGroup = root.findViewById(R.id.feelradioGroup)
        fevelradioGroup = root.findViewById(R.id.fevelradioGroup)
        coughradioGroup = root.findViewById(R.id.coughradioGroup)
        fatigueradioGroup = root.findViewById(R.id.fatigueradioGroup)
        breatheadioGroup = root.findViewById(R.id.breatheadioGroup)
        whereareadioGroup = root.findViewById(R.id.whereareadioGroup)
        sendButton.setOnClickListener(View.OnClickListener {
            if (edtmaildisplay.text.toString().isEmpty()) {
                Toast.makeText(activity, "Please enter email !", Toast.LENGTH_SHORT).show()
            } else if(howyoufeel.isEmpty()){
                Toast.makeText(activity, "Please choose how you feel !", Toast.LENGTH_SHORT).show()
            } else if(havefever.isEmpty()){
                Toast.makeText(activity, "Please choose if you have fever !", Toast.LENGTH_SHORT).show()
            } else {
                val headersList: ArrayList<String> = arrayListOf("Email", "How You Feel", "Have Fever","Body Temp","Have Cough","Usual Fatigue",
                    "Shortness Of Breathe","Where are you")
                val rowsList: ArrayList<String> = arrayListOf(edtmaildisplay.text.toString(), howyoufeel, havefever,edtbodytempdisplay.text.toString(),
                        cough,fatigue,breathe,wherear)
                val intent = Intent(activity, ReviewActivity::class.java)
                intent.putExtra("headerlist", headersList)
                intent.putExtra("contentlist", rowsList)
                intent.putExtra("title", "Self Diagnosis")
                startActivity(intent)
            }
        })
        fevelradioGroup.setOnCheckedChangeListener(checkedChangeListener)
        feelradioGroup.setOnCheckedChangeListener(checkedChangeListener2)
        coughradioGroup.setOnCheckedChangeListener(checkedChangeListener3)
        fatigueradioGroup.setOnCheckedChangeListener(checkedChangeListener4)
        whereareadioGroup.setOnCheckedChangeListener(checkedChangeListener5)
        breatheadioGroup.setOnCheckedChangeListener(checkedChangeListener6)
        return root
    }

    private val checkedChangeListener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
        val rb = group.findViewById<View>(checkedId) as RadioButton
        if (null != rb && checkedId > -1) {
            havefever = rb.text.toString()
        }
    }

    private val checkedChangeListener2 = RadioGroup.OnCheckedChangeListener { group, checkedId ->
        val rb = group.findViewById<View>(checkedId) as RadioButton
        if (null != rb && checkedId > -1) {
            howyoufeel = rb.text.toString()
        }
    }

    private val checkedChangeListener3 = RadioGroup.OnCheckedChangeListener { group, checkedId ->
        val rb = group.findViewById<View>(checkedId) as RadioButton
        if (null != rb && checkedId > -1) {
            cough = rb.text.toString()
        }
    }

    private val checkedChangeListener4 = RadioGroup.OnCheckedChangeListener { group, checkedId ->
        val rb = group.findViewById<View>(checkedId) as RadioButton
        if (null != rb && checkedId > -1) {
            fatigue = rb.text.toString()
        }
    }

    private val checkedChangeListener5 = RadioGroup.OnCheckedChangeListener { group, checkedId ->
        val rb = group.findViewById<View>(checkedId) as RadioButton
        if (null != rb && checkedId > -1) {
            wherear = rb.text.toString()
        }
    }

    private val checkedChangeListener6 = RadioGroup.OnCheckedChangeListener { group, checkedId ->
        val rb = group.findViewById<View>(checkedId) as RadioButton
        if (null != rb && checkedId > -1) {
            breathe = rb.text.toString()
        }
    }

    companion object {
        fun newInstance(): SelfDiagnosis = SelfDiagnosis()
    }
}