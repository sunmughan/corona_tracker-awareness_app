package com.quarantinealert.feature.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.quarantinealert.BuildConfig
import com.quarantinealert.R
import com.quarantinealert.config.AppPrefs
import com.quarantinealert.feature.home.HomeActivity
import com.quarantinealert.feature.slider.OnBoardingActivity
import ir.heydarii.appupdater.AppUpdaterDialog
import ir.heydarii.appupdater.pojo.Store
import ir.heydarii.appupdater.pojo.UpdaterStoreList
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by inject()

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, SplashActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            AppPrefs(from).setFirstTimeLaunch(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (AppPrefs(this).isFirstTimeLaunch()) {
            startActivity(Intent(this, OnBoardingActivity::class.java))
            finish()
        }
        setContentView(R.layout.activity_splash)
        setOnClick()
        setViewModel()
       // checkAppUpdate()
        viewModel.getData()

    }

    private fun setOnClick() {
        retryButton.setOnClickListener {
            viewModel.getData()
        }
    }

//    private fun setupUI() {
//        sign_out_button.setOnClickListener {
//            signOut()
//        }
//    }
//
//    private fun signOut() {
//        startActivity(SignInActivity.getLaunchIntent(this))
//        FirebaseAuth.getInstance().signOut();
//    }


    fun checkAppUpdate() {
        val link = "https://doc-0g-5o-docs.googleusercontent.com/docs/securesc/d16d04dhr82smbj7l1n13im983mpgku3/h78iqmok7gptn3g1c30dl7e3acrmoogc/1585833975000/15183274361675628968/15183274361675628968/1SNpW5kH0V9Jzl2TseyBtloSs5aksnQ2V?e=download&authuser=0&nonce=hkbiebfm2br3c&user=15183274361675628968&hash=kh09hneso3ha4lt6fj35t8fbns492i30"
        val list = ArrayList<UpdaterStoreList>()
        list.add(
            UpdaterStoreList(Store.DIRECT_URL, "Download now!", R.mipmap.ic_launcher, link, BuildConfig.APPLICATION_ID))
        AppUpdaterDialog.getInstance("New Update !", "Lots of new features! Update right now", list, true).show(supportFragmentManager, "updateError")
    }


    private fun setViewModel() {
        viewModel.apply {
            showLoadingLiveData.observe(this@SplashActivity, observerShowLoading())
            getDataSuccessfulLiveData.observe(this@SplashActivity, observerGetDataSuccessful())
            getDataFailedLiveData.observe(this@SplashActivity, observerGetDataFailed())
        }
    }

    private fun observerShowLoading() = Observer<Boolean> {
        if (it) {
            progressBar.visibility = View.VISIBLE
            retryButton.visibility = View.GONE
            messageErrorText.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            retryButton.visibility = View.VISIBLE
            messageErrorText.visibility = View.VISIBLE
        }
    }

    private fun observerGetDataSuccessful() = Observer<Boolean> {
        if (it) {
            goToHome()
        }
    }

    private fun observerGetDataFailed() = Observer<String> {
        messageErrorText.text = it
    }

    private fun goToHome() {
        finish()
        startActivity(Intent(this, HomeActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
