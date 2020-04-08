package com.quarantinealert.feature.slider

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.quarantinealert.R
import com.quarantinealert.config.AppPrefs
import com.quarantinealert.feature.slider.adapter.SliderAdapter
import com.quarantinealert.feature.splash.SplashActivity
import com.quarantinealert.hide
import com.quarantinealert.show
import kotlinx.android.synthetic.main.activity_onboarding.*


class OnBoardingActivity : AppCompatActivity() {

    private lateinit var sliderAdapter: SliderAdapter
    private var dots: Array<TextView?>? = null
    private lateinit var layouts: Array<Int>
    val RC_SIGN_IN: Int = 1
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    private val sliderChangeListener = object : OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            addBottomDots(position)

            if (position == layouts.size.minus(1)) {
                nextBtn.hide()
                skipBtn.hide()
                startBtn.show()
            } else {
                nextBtn.show()
                skipBtn.show()
                startBtn.hide()
            }
        }

        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        firebaseAuth = FirebaseAuth.getInstance()
        init()
        dataSet()
        interactions()
        configureGoogleSignIn()
    }

    //GoogleLoginWithFirebase

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

//    private fun setupUI() {
//        startBtn.setOnClickListener {
//            signIn()
//        }
//    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                startActivity(SplashActivity.getLaunchIntent(this))
            } else {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            startActivity(SplashActivity.getLaunchIntent(this))
            finish()
        }
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, OnBoardingActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    private fun init() {
        /** Layouts of the three onBoarding Screens.
         *  Add more layouts if you wish.
         **/
        layouts = arrayOf(
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4
        )

        sliderAdapter = SliderAdapter(this, layouts)
    }

    private fun dataSet() {
        /**
         * Adding bottom dots
         * */
        addBottomDots(0)

        slider.apply {
            adapter = sliderAdapter
            addOnPageChangeListener(sliderChangeListener)
        }
    }

    private fun interactions() {
        skipBtn.setOnClickListener {
            // Launch login screen
            signIn()
        }
        startBtn.setOnClickListener {
            // Launch login screen
            signIn()

        }
        nextBtn.setOnClickListener {
            /**
             *  Checking for last page, if last page
             *  login screen will be launched
             * */
            val current = getCurrentScreen(+1)
            if (current < layouts.size) {
                /**
                 * Move to next screen
                 * */
                slider.currentItem = current
            } else {
                // Launch login screen
                signIn()
            }
        }
    }

    private fun navigateToLogin() {
        AppPrefs(this).setFirstTimeLaunch(false)
        startActivity(Intent(this, SplashActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls(layouts.size)

        dotsLayout.removeAllViews()
        for (i in 0 until dots!!.size) {
            dots!![i] = TextView(this)
            dots!![i]?.text = Html.fromHtml("&#8226;")
            dots!![i]?.textSize = 35f
            dots!![i]?.setTextColor(resources.getColor(R.color.colorPrimary))
            dotsLayout.addView(dots!![i])
        }

        if (dots!!.isNotEmpty()) {
            dots!![currentPage]?.setTextColor(resources.getColor(R.color.grey))
        }
    }

    private fun getCurrentScreen(i: Int): Int = slider.currentItem.plus(i)


}
