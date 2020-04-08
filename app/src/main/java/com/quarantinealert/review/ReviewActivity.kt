package com.quarantinealert.review

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.quarantinealert.R
import com.quarantinealert.feature.home.HomeActivity
import com.quarantinealert.review.adapter.ReviewAdapter
import kotlinx.android.synthetic.main.activity_review.*
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class ReviewActivity : AppCompatActivity() {

    var tableString:String=""
    var headerList:ArrayList<String> = arrayListOf()
    var contentlist:ArrayList<String> = arrayListOf()
    lateinit var reciept:LinearLayout
    var sharePath: String? = ""
    lateinit var shareFile:File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        requestAppPermissions()
        requestReadStorageRuntimePermission()
        tooltitle.text = (intent.getStringExtra("title"))+" Form Review"
        header.text = (intent.getStringExtra("title"))+" Form"
        headerList = intent.getSerializableExtra("headerlist") as ArrayList<String>
        contentlist = intent.getSerializableExtra("contentlist") as ArrayList<String>
        reciept = findViewById(R.id.reciept)
        setRview()
        edit.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        fooddsendButton.setOnClickListener(View.OnClickListener {
            tableString = genrateText(headerList,contentlist)
            Sendemail().execute()
        })
    }

    private fun setRview(){
        reviewrview.layoutManager = LinearLayoutManager(this)
        reviewrview.itemAnimator = DefaultItemAnimator()
        val hotlineAdapter = ReviewAdapter(headerList,contentlist)
        reviewrview.adapter = hotlineAdapter
    }

    fun takess(): Bitmap {
        val view = reciept
        view.setDrawingCacheEnabled(true)
        val bitmap = Bitmap.createBitmap(view.getDrawingCache())
        view.setDrawingCacheEnabled(false)
        storess(bitmap,"Document"+System.currentTimeMillis()+".jpeg")
        return bitmap
    }

    fun storess(bm: Bitmap, filename: String) {
        val now = Date()
        DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)
        val filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots"
        val dir= File(filepath)
        if (!dir.exists())
            dir.mkdirs()
        val file = File(dir, filename)
        try {
            val fOut = FileOutputStream(file)
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()
            val newPath = file.path
            sharePath = newPath
            shareFile = file
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun genrateText(titleArrayList: ArrayList<String>,contentList: ArrayList<String>):String{
        var content:String=""
        val tempC:StringBuilder=StringBuilder()
        for (i in 0 until titleArrayList.size){
            val title = SpannableStringBuilder()
                .bold { append(titleArrayList.get(i)) }
            content ="<h2>"+title+"</h2>" +
                    "<p>"+contentList.get(i)+"</p>\n"
            tempC.append(content)
        }
        return Html.fromHtml(tempC.toString()).toString()
    }

    inner class Sendemail : AsyncTask<String?, Int?, Int?>() {
        var progressDialog: ProgressDialog? = null
        private var all_email: StringBuilder? = null
        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(this@ReviewActivity)
            progressDialog!!.setMessage("Processing, please wait...")
            progressDialog!!.show()
            takess()
        }

        override fun doInBackground(vararg strings: String?): Int {
            val props = Properties()
            props["mail.smtp.host"] = "smtp.gmail.com"
            props["mail.smtp.socketFactory.port"] = "465"
            props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
            props["mail.smtp.auth"] = "true"
            props["mail.smtp.starttls.enable"]="true"
            props["mail.smtp.port"] = "465"
            val session: Session = Session.getInstance(props,
                object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication("c19care@gmail.com", "(#SUNMUGHANS#)")
                    }
                })
            try {
                val message: Message = MimeMessage(session)
                message.setFrom(InternetAddress("c19care@gmail.com"))
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("c19care@gmail.com"))
                if (intent.getStringExtra("title").equals("Contribution")){
                    message.setSubject("Contribution Form")
                } else if(intent.getStringExtra("title").equals("Self Diagnosis")){
                    message.setSubject("Self Diagnosis Form")
                } else if(intent.getStringExtra("title").equals("Volunteer")){
                    message.setSubject("Volunteer Form")
                } else if(intent.getStringExtra("title").equals("Food Donation")){
                    message.setSubject("Food Donation Form")
                } else if (intent.getStringExtra("title").equals("Sanitizer Donation")){
                    message.setSubject("Sanitizer Donation Form")
                }else if(intent.getStringExtra("title").equals("Medicine Donation")){
                    message.setSubject("Medicine Donation Form")
                } else if(intent.getStringExtra("title").equals("Funds Donation")){
                    message.setSubject("Funds Donation Form")
                } else {
                    //
                }
                val multipart =  MimeMultipart()
                val messageBodyPart = MimeBodyPart()
                val fileBodyPart = MimeBodyPart()
                val source = FileDataSource(sharePath)
                fileBodyPart.attachFile(shareFile)
                fileBodyPart.setDataHandler(DataHandler(source))
                fileBodyPart.setFileName(shareFile.name)
                messageBodyPart.setText(tableString)
                multipart.addBodyPart(messageBodyPart)
                multipart.addBodyPart(fileBodyPart)
                message.setContent(multipart)
                Transport.send(message)
                println("Done")
                Log.e("filepath",sharePath+shareFile.name+" "+multipart+" "+messageBodyPart+" "+message )
            } catch (e: MessagingException) {
                throw RuntimeException(e)
            }
            return 1
        }

        override fun onPostExecute(integer: Int?) {
            super.onPostExecute(integer)
            progressDialog!!.dismiss()
            tableString=""
            Toast.makeText(this@ReviewActivity,"Submitted successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@ReviewActivity, HomeActivity::class.java))
        }
    }


    fun requestReadStorageRuntimePermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100
            )
        } else {
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: kotlin.IntArray) {
        when (requestCode) {
            100 -> {
                if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return
            }
            100 -> {
                if (grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                } else {
                }
                return
            }
        }
    }

    private fun requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 100
        ) // your request code
    }

    private fun hasReadPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            baseContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasWritePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            baseContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

}
