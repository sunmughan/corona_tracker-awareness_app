package com.quarantinealert.feature.feedback

import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.quarantinealert.R
import com.quarantinealert.util.hideKeyboard
import kotlinx.android.synthetic.main.activity_feedback.*
import org.koin.android.ext.android.inject
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class FeedbackActivity : AppCompatActivity() {

    private val viewModel: FeedbackViewModel by inject()
    var getemail:String=""
    var getmessage:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        setViewModel()
        setOnClick()
    }

    private fun setOnClick() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        sendButton.setOnClickListener {
            sendFeedback()
        }
    }

    private fun setViewModel() {
//        viewModel.showLoadingLiveData.observe(this, observerShowLoading())
//        viewModel.sendFeedbackSuccessfulLiveData.observe(this, observerSendFeedbackSuccessful())
//        viewModel.sendFeedbackFailedLiveData.observe(this, observerSendFeedbackFailed())
    }

    private fun sendFeedback() {
        hideKeyboard(this)
        val email = emailEdit.text.toString()
        val message = feedbackEdit.text.toString()

        if (!isValidEmail(email)) {
            Toast.makeText(this,getString(R.string.feedback_email_error),Toast.LENGTH_SHORT).show()
            return
        }

        if (message.isBlank()) {
            Toast.makeText(this,getString(R.string.feedback_feedback_error),Toast.LENGTH_SHORT).show()
            return
        }
        getemail = email
        getmessage = message
        Sendemail().execute()
    }

    inner class Sendemail : AsyncTask<String?, Int?, Int?>() {
        var progressDialog: ProgressDialog? = null
        private var all_email: StringBuilder? = null
        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(this@FeedbackActivity)
            progressDialog!!.setMessage("Processing, please wait...")
            progressDialog!!.show()
        }

        override fun doInBackground(vararg strings: String?): Int {
            val props = Properties()
            props["mail.smtp.host"] = "smtp.gmail.com"
            props["mail.smtp.socketFactory.port"] = "465"
            props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
            props["mail.smtp.auth"] = "true"
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
                message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("c19care@gmail.com")
                )
                message.setSubject("Feedback from "+getemail)
                message.setText(getmessage)
                Transport.send(message)
                println("Done")
            } catch (e: MessagingException) {
                throw RuntimeException(e)
            }
            return 1
        }

        override fun onPostExecute(integer: Int?) {
            super.onPostExecute(integer)
            progressDialog!!.dismiss()
            Toast.makeText(this@FeedbackActivity, this@FeedbackActivity.getString(R.string.feedback_send_successful), Toast.LENGTH_SHORT).show()
            onBackPressed()


        }
    }


    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    /** Observers **/


    private fun observerSendFeedbackSuccessful() = Observer<String> {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun observerSendFeedbackFailed() = Observer<String> {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }
}
