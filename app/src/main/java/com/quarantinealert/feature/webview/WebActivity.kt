package com.quarantinealert.feature.webview

import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.quarantinealert.R
import kotlinx.android.synthetic.main.activity_myth.toolbar
import kotlinx.android.synthetic.main.activity_web.*
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class WebActivity : AppCompatActivity() {

    lateinit var webView: WebView
    lateinit var webSettings: WebSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        setOnClick()
        val url = intent.getStringExtra("url")!!
        val title = intent.getStringExtra("title")!!
        val value = intent.getStringExtra("value")!!
        val toolbar:androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        webView = findViewById(R.id.wvGetmeanapp)
        webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.domStorageEnabled = true
        webSettings.saveFormData = true
        webSettings.allowContentAccess = true
        webSettings.allowFileAccess = true
        webSettings.useWideViewPort = true
        webSettings.setSupportZoom(true)
        webSettings.allowFileAccessFromFileURLs = true
        webSettings.allowUniversalAccessFromFileURLs = true

        toolbar.title = title
        Log.e("weburl", url)
     //   webView.loadUrl("file:///android_asset/error.html")
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                Log.e("logeweburl", "shouldOverrideUrlLoading: $url")
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                progressBar.visibility = View.GONE
            }

            override fun onReceivedError(view: WebView,
                                         errorCode: Int,
                                         description: String,
                                         failingUrl: String) {
                Toast.makeText(this@WebActivity, "Error:$description", Toast.LENGTH_SHORT).show()
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                if (handler != null){
                    handler.proceed()
                } else {
                    super.onReceivedSslError(view, null, error)
                }

            }

        }
        if(value.equals("pdf")){
            var loadurl = ""
            try {
                loadurl = URLEncoder.encode(url, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$loadurl")
        } else {
            webView.loadUrl(url)
        }

    }

    private fun setOnClick() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
