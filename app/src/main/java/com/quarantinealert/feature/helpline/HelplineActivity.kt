package com.quarantinealert.feature.helpline

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.quarantinealert.R
import com.quarantinealert.feature.helpline.adapter.HotlineAdapter
import com.quarantinealert.feature.helpline.model.Hotline
import kotlinx.android.synthetic.main.activity_helpline.*

class HelplineActivity : AppCompatActivity() {

    val hotlineList: List<Hotline> = listOf(
    Hotline("Andhra Pradesh", "0866-2410978", R.drawable.ic_kementerian_kesehatan),
    Hotline("Arunachal Pradesh", "9436055743", R.drawable.ic_kementerian_kesehatan),
    Hotline("Assam", "6913347770", R.drawable.ic_kementerian_kesehatan),
    Hotline("Bihar", "104", R.drawable.ic_kementerian_kesehatan),
    Hotline("Chhattisgarh", "104", R.drawable.ic_kementerian_kesehatan),
    Hotline("Goa", "104", R.drawable.ic_kementerian_kesehatan),
    Hotline("Gujarat", "104", R.drawable.ic_kementerian_kesehatan),
    Hotline("Haryana", "8558893911", R.drawable.ic_kementerian_kesehatan),
    Hotline("Himachal Pradesh", "104", R.drawable.ic_kementerian_kesehatan),
    Hotline("Jharkhand", "104", R.drawable.ic_kementerian_kesehatan),
    Hotline("Karnataka", "104", R.drawable.ic_kementerian_kesehatan),
    Hotline("Kerala", "0471-2552056", R.drawable.ic_kementerian_kesehatan),
    Hotline("Madhya Pradesh", "755-2527177", R.drawable.ic_kementerian_kesehatan),
    Hotline("Maharashtra", "020-26127394", R.drawable.ic_kementerian_kesehatan),
    Hotline("Manipur", "3852411668", R.drawable.ic_kementerian_kesehatan),
    Hotline("Meghalaya", "108", R.drawable.ic_kementerian_kesehatan),
    Hotline("Mizoram", "102", R.drawable.ic_kementerian_kesehatan),
    Hotline("Nagaland", "7005539653", R.drawable.ic_kementerian_kesehatan),
    Hotline("Odisha", "9439994859", R.drawable.ic_kementerian_kesehatan),
    Hotline("Punjab", "104", R.drawable.ic_kementerian_kesehatan),
    Hotline("Rajasthan", "0141-2225624", R.drawable.ic_kementerian_kesehatan),
    Hotline("Sikkim", "104", R.drawable.ic_kementerian_kesehatan),
    Hotline("Tamil Nadu", "044-29510500", R.drawable.ic_kementerian_kesehatan),
    Hotline("Telangana", "104", R.drawable.ic_kementerian_kesehatan),
    Hotline("Tripura", "0381-2315879", R.drawable.ic_kementerian_kesehatan),
    Hotline("Uttarakhand", "104", R.drawable.ic_kementerian_kesehatan),
    Hotline("Uttar Pradesh", "18001805145", R.drawable.ic_kementerian_kesehatan),
    Hotline("West Bengal", "1800313444222, 03323412600", R.drawable.ic_kementerian_kesehatan),

    Hotline("Andaman and Nicobar Islands", "03192-232102", R.drawable.ic_kementerian_kesehatan),
    Hotline("Chandigarh", "9779558282", R.drawable.ic_kementerian_kesehatan),
    Hotline("Dadra and Nagar Haveli and Daman & Diu", "104", R.drawable.ic_kementerian_kesehatan),
    Hotline("Delhi", "011-22307145", R.drawable.ic_kementerian_kesehatan),
    Hotline("Jammu & Kashmir", "01912520982, 0194-2440283", R.drawable.ic_kementerian_kesehatan),
    Hotline("Ladakh", "01982256462", R.drawable.ic_kementerian_kesehatan),
    Hotline("Lakshadweep", "104", R.drawable.ic_kementerian_kesehatan),
    Hotline("Puducherry", "104", R.drawable.ic_kementerian_kesehatan)
    )

    var catarray: ArrayList<Hotline>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_helpline)
        setOnClick()

        rv_detail_hotline.layoutManager = LinearLayoutManager(this)
        rv_detail_hotline.itemAnimator = DefaultItemAnimator()
        val hotlineAdapter = HotlineAdapter(hotlineList)
        rv_detail_hotline.adapter = hotlineAdapter
        hotlineAdapter.setItemClickListener(object :HotlineAdapter.OnItemClickListener{
            override fun onItemClick(hotline: String) {
                val intent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.fromParts("tel",hotline, null))

                if (ContextCompat.checkSelfPermission(this@HelplineActivity, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this@HelplineActivity, arrayOf(android.Manifest.permission.CALL_PHONE), 1)

                } else {
                    startActivity(intent)
                    //Toast.makeText(this@HelplineActivity,"Not allowed",Toast.LENGTH_SHORT).show()
                }
            }

        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {

            1 -> {

                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT)
                        .show()
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    private fun setOnClick() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }
}
