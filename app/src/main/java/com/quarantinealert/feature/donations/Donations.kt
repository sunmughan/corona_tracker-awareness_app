package com.quarantinealert.feature.donations

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.quarantinealert.R
import com.quarantinealert.review.ReviewActivity
import com.quarantinealert.util.DateTimePicker
import kotlinx.android.synthetic.main.activity_donations.*
import kotlinx.android.synthetic.main.activity_donations.toolbar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Donations : AppCompatActivity() {

    lateinit var foodqspinner:Spinner
    lateinit var medtypespinner:Spinner
    lateinit var donaspinner:Spinner
    var tableString:String=""
    var foodqaulity:String=""
    var medtype:String=""
    var value:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donations)
        val doptions = resources.getStringArray(R.array.doptions)
        val foodarray = resources.getStringArray(R.array.foodarray)
        val medtypearray = resources.getStringArray(R.array.medtypearray)
        foodqspinner = findViewById(R.id.foodqspinner)
        medtypespinner = findViewById(R.id.medtypespinner)
        donaspinner = findViewById(R.id.donationspinner)
        setSpinner(donaspinner,doptions)
        setSpinner(foodqspinner,foodarray)
        setSpinner(medtypespinner,medtypearray)
        setNumOnText()
        setOnClick()
        fooddsendButton.setOnClickListener(View.OnClickListener {
            if (foodqtext.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter quantity !", Toast.LENGTH_SHORT).show()
            } else if(expdatetext.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter expiry date !", Toast.LENGTH_SHORT).show()
            } else if(addtext.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter your address !", Toast.LENGTH_SHORT).show()
            } else if(donorname.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter a your name !", Toast.LENGTH_SHORT).show()
            } else if(foodqaulity.isEmpty()){
                Toast.makeText(this, "Please choose food quality !", Toast.LENGTH_SHORT).show()
            } else {
                val headersList: ArrayList<String> = arrayListOf("Quantity", "Expiry date", "Address", "Name", "Food quality")
                val rowsList: ArrayList<String> = arrayListOf(foodqtext.text.toString(), expdatetext.text.toString(), addtext.text.toString(), donorname.text.toString(), foodqaulity)
                val intent = Intent(this, ReviewActivity::class.java)
                intent.putExtra("headerlist", headersList)
                intent.putExtra("contentlist", rowsList)
                intent.putExtra("title", "Food Donation")
                startActivity(intent)
            }
        })
        sansendButton.setOnClickListener(View.OnClickListener {
            if (santiqtext.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter quantity !", Toast.LENGTH_SHORT).show()
            } else if(santexpdtext.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter expiry date !", Toast.LENGTH_SHORT).show()
            } else if(sanaddtext.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter your address !", Toast.LENGTH_SHORT).show()
            } else if(sandonorname.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter a your name !", Toast.LENGTH_SHORT).show()
            } else {
                val headersList: ArrayList<String> = arrayListOf("Quantity", "Expiry date", "Address", "Name")
                val rowsList: ArrayList<String> = arrayListOf(santiqtext.text.toString(), santexpdtext.text.toString(), sanaddtext.text.toString(), sandonorname.text.toString())
                val intent = Intent(this, ReviewActivity::class.java)
                intent.putExtra("headerlist", headersList)
                intent.putExtra("contentlist", rowsList)
                intent.putExtra("title", "Sanitizer Donation")
                startActivity(intent)
            }
        })
        medsendButton.setOnClickListener(View.OnClickListener {
            if (mednametext.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter medicine name !", Toast.LENGTH_SHORT).show()
            } else if(medbrandtext.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter medicine brand name !", Toast.LENGTH_SHORT).show()
            } else if(medtype.isEmpty()){
                Toast.makeText(this, "Please choose medicine type !", Toast.LENGTH_SHORT).show()
            } else if (medqtext.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter quantity !", Toast.LENGTH_SHORT).show()
            } else if(medexpdtext.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter expiry date !", Toast.LENGTH_SHORT).show()
            } else if(meddaddtext.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter your address !", Toast.LENGTH_SHORT).show()
            } else if(meddonorname.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter a your name !", Toast.LENGTH_SHORT).show()
            } else {
                val headersList: ArrayList<String> = arrayListOf("Medicine name", "Medicine brand name", "Medicine type", "Quantity","Expiry date","Address","Name")
                val rowsList: ArrayList<String> = arrayListOf(mednametext.text.toString(), medbrandtext.text.toString(),medtype, medqtext.text.toString(), medexpdtext.text.toString(),meddaddtext.text.toString(),meddonorname.text.toString())
                val intent = Intent(this, ReviewActivity::class.java)
                intent.putExtra("headerlist", headersList)
                intent.putExtra("contentlist", rowsList)
                intent.putExtra("title", "Medicine Donation")
                startActivity(intent)
            }
        })
        funddsendButton.setOnClickListener(View.OnClickListener {
//            if (funddonorname.text.toString().isEmpty()) {
//                Toast.makeText(this, "Please enter your name !", Toast.LENGTH_SHORT).show()
//            } else if(funddaddtext.text.toString().isEmpty()){
//                Toast.makeText(this, "Please enter your address !", Toast.LENGTH_SHORT).show()
//            } else if(reasondtext.text.toString().isEmpty()){
//                Toast.makeText(this, "Please enter the reason to donate !", Toast.LENGTH_SHORT).show()
//            } else {
//                val headersList: ArrayList<String> = arrayListOf("Name", "Address", "Reason to donate")
//                val rowsList: ArrayList<String> = arrayListOf(funddonorname.text.toString(), funddaddtext.text.toString(), reasondtext.text.toString())
//                val intent = Intent(this, ReviewActivity::class.java)
//                intent.putExtra("headerlist", headersList)
//                intent.putExtra("contentlist", rowsList)
//                intent.putExtra("title", "Funds Donation")
//                startActivity(intent)
                paytmOpen(this)


//            }
        })
        expdatetext.setOnClickListener(View.OnClickListener {
            DateTimePicker(this, true){
                val sdf = SimpleDateFormat(DateTimePicker.getFormat("dt"), Locale.getDefault())
                expdatetext.setText(sdf.format(it.calendar.time))
            }.show()
        })
        santexpdtext.setOnClickListener(View.OnClickListener {
            DateTimePicker(this, true){
                val sdf = SimpleDateFormat(DateTimePicker.getFormat("dt"), Locale.getDefault())
                santexpdtext.setText(sdf.format(it.calendar.time))
            }.show()
        })
        medexpdtext.setOnClickListener(View.OnClickListener {
            DateTimePicker(this, true){
                val sdf = SimpleDateFormat(DateTimePicker.getFormat("dt"), Locale.getDefault())
                medexpdtext.setText(sdf.format(it.calendar.time))
            }.show()
        })

    }

    fun paytmOpen(context: Context){
        val url = "https://paytm.com/helpinghand/pm-cares-fund"
        try {
            val pm = context.packageManager
            pm.getPackageInfo("net.one97.paytm", PackageManager.GET_ACTIVITIES)
            val i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            startActivity(i)
        } catch (e:PackageManager.NameNotFoundException) {
            Toast.makeText(context, "Paytm app not installed in your phone", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun setOnClick() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    private fun setSpinner(spinner: Spinner,array: Array<String>){
        if (spinner != null) {
            val adapter = ArrayAdapter(this, R.layout.spinner_layout, R.id.spintext, array)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val text = array[position]
                if(spinner.id==R.id.donationspinner){
                    if(text.equals("Foods")){
                        foodlay.visibility=View.VISIBLE
                        santizielay.visibility=View.GONE
                        medicinelay.visibility=View.GONE
                        fundslay.visibility=View.GONE
                    } else if(text.equals("Sanitizers")){
                        foodlay.visibility=View.GONE
                        santizielay.visibility=View.VISIBLE
                        medicinelay.visibility=View.GONE
                        fundslay.visibility=View.GONE
                    } else if(text.equals("Medicines")){
                        foodlay.visibility=View.GONE
                        santizielay.visibility=View.GONE
                        medicinelay.visibility=View.VISIBLE
                        fundslay.visibility=View.GONE
                    } else if(text.equals("Funds")) {
                        foodlay.visibility=View.GONE
                        santizielay.visibility=View.GONE
                        medicinelay.visibility=View.GONE
                        fundslay.visibility=View.VISIBLE
                    } else {
                        //do nothing
                    }

                } else if(spinner.id==R.id.foodqspinner){
                    foodqaulity=text
                } else if(spinner.id==R.id.medtypespinner){
                    medtype=text
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun setNumOnText(){
        val spanstring = SpannableString("For any queries please contact +919584215603")
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+919584215603", null))
                if (ContextCompat.checkSelfPermission(this@Donations, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this@Donations, arrayOf(android.Manifest.permission.CALL_PHONE), 1)
                } else {
                    startActivity(intent)
                    //Toast.makeText(this@HelplineActivity,"Not allowed",Toast.LENGTH_SHORT).show()
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }
        }
        spanstring.setSpan(ForegroundColorSpan(resources.getColor(R.color.colorPrimary)), 31, 44, 0)
        spanstring.setSpan(BackgroundColorSpan(Color.WHITE), 31, 44, 0)
        spanstring.setSpan(clickableSpan, 31, 44, 0)
        callforquery.movementMethod = LinkMovementMethod.getInstance()
        callforquery.text = spanstring
    }
}
