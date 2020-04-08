package com.quarantinealert.feature.home

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources.NotFoundException
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.quarantinealert.R
import com.quarantinealert.feature.credits.CreditsActivity
import com.quarantinealert.feature.donations.Donations
import com.quarantinealert.feature.emergency.EmergencyActivity
import com.quarantinealert.feature.feedback.FeedbackActivity
import com.quarantinealert.feature.forms.FormsActivity
import com.quarantinealert.feature.global.GlobalCasesActivity
import com.quarantinealert.feature.helpline.HelplineActivity
import com.quarantinealert.feature.media.MediaActivity
import com.quarantinealert.feature.myth.FAQActivity
import com.quarantinealert.feature.myth.MythActivity
import com.quarantinealert.feature.search.SearchActivity
import com.quarantinealert.feature.stages.StagesActivity
import com.quarantinealert.feature.symptoms.SymptomsActivity
import com.quarantinealert.feature.webview.WebActivity
import com.quarantinealert.feature.yoga.YogaActivity
import com.quarantinealert.firebase.analytics.AnalyticsUtil
import com.quarantinealert.model.Country
import com.quarantinealert.model.TotalCases
import com.quarantinealert.model.YouTubeModel
import com.quarantinealert.util.changeBitmap
import com.quarantinealert.util.formatNumber
import com.quarantinealert.util.location.SmartLocation
import com.quarantinealert.util.setTransparentStatusBar
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.home_layout.*
import org.json.JSONObject
import org.koin.android.ext.android.inject
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class HomeActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel: HomeViewModel by inject()

    private var smartLocation: SmartLocation? = null

    private var mMap: GoogleMap? = null
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private var afterMarkerSelected: Marker? = null
    lateinit var recyclerView: RecyclerView
    lateinit var fablocation: FloatingActionButton
    lateinit var stages_rview: RecyclerView
    var stageAdapter: YoutubeRecyclerAdapter? = null
    var mRecyclerAdapter: YoutubeRecyclerAdapter? = null
    var videoArrayList: ArrayList<YouTubeModel> = arrayListOf()
    private var countrySelected: Country? = null
    var count = 0
    lateinit var latLngNow: LatLng
    lateinit var locationManager: LocationManager
    val links = arrayListOf(
        "https://youtu.be/OOJqHPfG7pA",
        "https://youtu.be/BtN-goy9VOY",
        "https://youtu.be/V3BXqYzTTSQ",
        "https://youtu.be/cuJ4WcsghL4",
        "https://youtu.be/Xw7IW0SMp-I"
    )
    val titleArray = arrayListOf(
        "How soap kills the coronavirus",
        "What Coronavirus Symptoms Look Like, Day By Day",
        "The Coronavirus Explained & What You Should Do",
        "Why Coronavirus Is Dangerous For Diabetics",
        "PM Modi's 3-Minute Plan To Fight Coronavirus | ABP News",
        "PM Narendra Modi's address to the Nation on #COVID19 | #CoronaVirus | #StayHomeStaySafe | #StayHome"
    )
    val thumbArray = arrayListOf(
        "https://i.ytimg.com/vi/-LKVUarhtvE/hqdefault.jpg",
        "https://i.ytimg.com/vi/OOJqHPfG7pA/hqdefault.jpg",
        "https://i.ytimg.com/vi/BtN-goy9VOY/hqdefault.jpg",
        "https://i.ytimg.com/vi/V3BXqYzTTSQ/hqdefault.jpg",
        "https://i.ytimg.com/vi/cuJ4WcsghL4/hqdefault.jpg",
        "https://i.ytimg.com/vi/Xw7IW0SMp-I/hqdefault.jpg"
    )
    val idArray = arrayListOf(
        "-LKVUarhtvE",
        "OOJqHPfG7pA",
        "BtN-goy9VOY",
        "V3BXqYzTTSQ",
        "cuJ4WcsghL4",
        "Xw7IW0SMp-I"
    )
    lateinit var donateus:MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        recyclerView = findViewById(R.id.videos_rview)
        fablocation = findViewById(R.id.fabloc)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        setTransparentStatusBar(false)
        setBottomHomeFragment()
        setDrawerLayout()
        setViewModel()
        setMap()
        requestLocationWithPermission()
        for (i in 0 until titleArray.size) {
            videoArrayList.add(
                YouTubeModel(
                    titleArray.get(i),
                    i.toString(),
                    idArray.get(i),
                    thumbArray.get(i)
                )
            )
            // Log.e("ytarray2",titleArray.get(i)+" "+idArray.get(i)+" "+thumbArray.get(i))
        }
        Log.e("ytarray", videoArrayList.toString())
        mRecyclerAdapter = YoutubeRecyclerAdapter(this, videoArrayList)
        recyclerView.adapter = mRecyclerAdapter
        Glide.with(this)
            .load(R.raw.click)
            .into(clickme)
        val navigationView = findViewById(R.id.navigationView) as NavigationView
        val headerview = navigationView.getHeaderView(0)
        donateus =  headerview.findViewById(R.id.donateus) as MaterialButton
        setOnClick()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RQ_SEARCH && resultCode == Activity.RESULT_OK) {
            val country = data?.getParcelableExtra<Country>(SearchActivity.SEARCH_RESULT)
            country?.run {
                if (location == getString(R.string.your_location)) {
                    AnalyticsUtil.logEventAction(
                        this@HomeActivity,
                        AnalyticsUtil.Value.SEARCH,
                        AnalyticsUtil.Value.YOUR_LOCATION
                    )
                    requestLocationWithPermission()
                } else {
                    countrySelected = this
                    AnalyticsUtil.logEventAction(
                        this@HomeActivity,
                        AnalyticsUtil.Value.SEARCH,
                        location
                    )
                    titleCasesText.text = getString(R.string.home_bottom_country_title, location)
                    confirmedCountText.text = formatNumber(confirmed)
                    deathCountText.text = formatNumber(deaths)
                    recoveredCountText.text = formatNumber(recovered)

                    val latLngNow = LatLng(latitude, longitude)

                    val location = CameraUpdateFactory.newLatLngZoom(
                        latLngNow, ZOOM_MAP
                    )

                    mMap?.animateCamera(location)
                }
            }
        }
        if (requestCode == SmartLocation.RQ_RESOLUTION_REQUIRED && resultCode == Activity.RESULT_OK) {
            smartLocation?.activateGPS()
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView)
        } else {
            moveTaskToBack(true)
        }
    }

    /** Init & set methods **/


    fun setRTView() {
        Log.e("arraylink", links.size.toString())
        var error: String
        var yttitle: String = ""
        var thumbnail_url: String = ""
        videoArrayList.clear()
        for (i in 0 until links.size) {
            val queue = Volley.newRequestQueue(this)
            val url: String = "http://www.youtube.com/oembed?url=" + links.get(i) + "&format=json"
            val stringReq = StringRequest(
                Request.Method.GET, url,
                Response.Listener<String> { response ->

                    val strResp = response.toString()
                    val jsonObj: JSONObject = JSONObject(strResp)
                    yttitle = jsonObj.getString("title")
                    thumbnail_url = jsonObj.getString("thumbnail_url")
                    videoArrayList.add(
                        YouTubeModel(
                            jsonObj.getString("title"),
                            i.toString(),
                            getYTid(links.get(i)),
                            jsonObj.getString("thumbnail_url")
                        )
                    )

                },
                Response.ErrorListener { error = "That didn't work!" })
            queue.add(stringReq)
            Log.e("hello", videoArrayList.toString())

        }


        //Log.e("arrayurl",videoArrayList.toString()+" "+yttitle+" "+thumbnail_url)

    }

    fun getYTid(url: String): String {
        val pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern: Pattern = Pattern.compile(pattern)
        val matcher: Matcher = compiledPattern.matcher(url)
        if (matcher.find()) {
            return matcher.group()
        } else {
            return "error"
        }
    }

    private fun setBottomHomeFragment() {
        val mAnimation = TranslateAnimation(
            TranslateAnimation.ABSOLUTE, 0f,
            TranslateAnimation.ABSOLUTE, 0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.001f)
        mAnimation.duration = 1000
        mAnimation.repeatCount = -1
        mAnimation.repeatMode = Animation.ZORDER_NORMAL
        mAnimation.interpolator = LinearInterpolator()
        updown.animation = mAnimation
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet)
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
                fablocation.animate().scaleX(1 - p1).scaleY(1 - p1).setDuration(0).start()
            }

            override fun onStateChanged(p0: View, p1: Int) {
                if (p1 == BottomSheetBehavior.STATE_EXPANDED) {
                    updown.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp)
                    text.text="Pull me down"
                } else {
                    updown.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
                    text.text="Pull me up"
                }
            }

        })
        fablocation.setOnClickListener(View.OnClickListener {
            val location = CameraUpdateFactory.newLatLngZoom(
                latLngNow, ZOOM_MAP
            )
            mMap?.animateCamera(location)
        })

    }


    private fun setDrawerLayout() {
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_global_cases -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.MENU_GLOBAL_CASES)
                    goToGlobalCases()
                }
                R.id.nav_symptoms -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.MENU_SYMPTOMS)
                    goToSymptoms()
                }
                R.id.nav_emergency_guide -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.MENU_EMERGENCY)
                    goToEmergencyGuide()
                }
                R.id.nav_feedback -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.MENU_FEEDBACK)
                    goToFeedback()
                }
                R.id.nav_forms -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.MENU_FEEDBACK)
                    goToForms()
                }
                R.id.nav_credits -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.MENU_CREDITS)
                    goToCredits()
                }
                R.id.nav_contact -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.MENU_CONTACT)
                    sendEmail()
                }

                R.id.inidaTracker -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_WEB)
                    goToWeb("https://www.covid19india.org/","INDIA COVID-19 TRACKER")
                }

                R.id.nav_helpline -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_HELPLINE)
                    goToHelp()
                }

                R.id.nav_donations -> {
                    goToDonations()
                }

                R.id.nav_precautions -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_MYTH)
                    goToMyth("precautions")
                }
                R.id.nav_guidelines -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_MYTH)
                    goToMyth("guidelines")
                }
                R.id.nav_aminfected -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_MYTH)
                    goToMyth("aminfected")
                }
                R.id.nav_knowinfected -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_MYTH)
                    goToMyth("knowinfected")
                }
                R.id.nav_hisolation -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_MYTH)
                    goToMyth("hisolation")
                }
                R.id.nav_telegram -> {
                    startActivity(telegramIntent(this))

                }
                R.id.nav_whatsapp -> {
                    openwp()
                }
                R.id.  nav_report -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_WEB)
                    goToWeb("https://docs.google.com/forms/d/1HVXi3-0ckxGTS81yAN1RmgrVnvg9cg2pyPTuPgBeVEo/viewform?edit_requested=true", "Report Cases")
                }

            }
            drawerLayout.closeDrawer(navigationView)
            true
        }
    }

    private fun setViewModel() {
        viewModel.showTotalCasesLiveData.observe(this, observerTotalCases())
        viewModel.getCountriesLiveData.observe(this, observerGetCountries())
        viewModel.getTotalCases()

    }


    private fun setMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
    }

    private fun setOnClick() {
        menuButton.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.MENU_OPEN)
            drawerLayout.openDrawer(navigationView)
        }
        searchText.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_OPEN_SEARCH)
            goToSearch()
        }
        globalCasesView.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_GLOBAL_CASES)
            goToGlobalCases()
        }
        symptomsView.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_SYMPTOMS)
            goToSymptoms()
        }

        stagesView.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_STAGES)
            goToStages("1")
        }
        stage1.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_STAGES)
            goToStages("1")
        }
        stage2.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_STAGES)
            goToStages("2")
        }
        stage3.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_STAGES)
            goToStages("3")
        }
        stage4.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_STAGES)
            goToStages("4")
        }
        consultButton.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_SYMPTOMS_BUTTON)
            goToSymptoms()
        }
        consultButton1.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_MYTH)
            goToMyth("myth")
        }
        mythheader.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_MYTH)
            goToMyth("myth")
        }
        mediaView.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_MEDIA)
            goToMedia()
        }
        gotoyoga.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_MEDIA)
            goToYoga()
        }
        consultButton7.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_YOGA)
            goToYoga()
        }
        donateus.setOnClickListener {
            paytmOpen(this)
        }

        emergencyView.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_EMERGENCY)
            goToEmergencyGuide()
        }
        knowMoreButton.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_EMERGENCY_BUTTON)
            goToEmergencyGuide()
        }
        faqbtn.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_FAQ)
            goToFAQ()
        }
        faqheader.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_FAQ)
            goToFAQ()
        }
        helplineheader.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_HELPLINE)
            goToHelp()
        }
        consultButton3.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_HELPLINE)
            goToHelp()
        }

    }

    @AfterPermissionGranted(RQ_ACCESS_FINE_LOCATION)
    private fun requestLocationWithPermission() {
        val perms = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            startLocationUpdates()
        } else {
            EasyPermissions.requestPermissions(
                this, "Debe otorgar permisos para acceder a su ubicación",
                RQ_ACCESS_FINE_LOCATION, *perms
            )
        }
    }

    private fun startLocationUpdates() {
        smartLocation = SmartLocation(this)
        smartLocation?.addOnGetLocationSuccessful {
            latLngNow = LatLng(it.latitude, it.longitude)

            val location = CameraUpdateFactory.newLatLngZoom(
                latLngNow, ZOOM_MAP
            )

            mMap?.animateCamera(location)

        }?.addOnGetLocationFailed {
            Log.i(SmartLocation.TAG, it.message ?: "")
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.apply {

            try {
                val success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        this@HomeActivity, R.raw.map_style
                    )
                )
                if (!success) {
                    Log.e(this@HomeActivity.javaClass.simpleName, "Style parsing failed.")
                }
            } catch (e: NotFoundException) {
                Log.e(this@HomeActivity.javaClass.simpleName, "Can't find style. Error: ", e)
            }

            uiSettings.isCompassEnabled = false
            setMinZoomPreference(1f)

            setOnMarkerClickListener {
                setSmallMarkerIcon()
                it.setIcon(
                    BitmapDescriptorFactory.fromBitmap(
                        changeBitmap(
                            this@HomeActivity,
                            R.drawable.ic_marker, 100, 100
                        )
                    )
                )

                viewModel.data[it]?.apply {
                    countrySelected = this
                    titleCasesText.text = getString(R.string.home_bottom_country_title, location)
                    confirmedCountText.text = formatNumber(confirmed)
                    deathCountText.text = formatNumber(deaths)
                    recoveredCountText.text = formatNumber(recovered)
                }

                afterMarkerSelected = it
                false
            }

            setOnMapClickListener {
                setSmallMarkerIcon()
                countrySelected = null
                viewModel.getTotalCases()
            }
        }

        viewModel.getCountryCases()
    }

    private fun setSmallMarkerIcon() {
        afterMarkerSelected?.run {
            setIcon(
                BitmapDescriptorFactory.fromBitmap(
                    changeBitmap(
                        this@HomeActivity,
                        R.drawable.ic_marker, 80, 80
                    )
                )
            )
        }
    }

    fun telegramIntent(context: Context): Intent {
        var intent: Intent? = null
        try {
            try {
                context.packageManager.getPackageInfo("org.telegram.messenger", 0)//Check for Telegram Messenger App
            } catch (e : Exception){
                context.packageManager.getPackageInfo("org.thunderdog.challegram", 0)//Check for Telegram X App
            }
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=MyGovCoronaNewsdesk"))
        }catch (e : Exception){ //App not found open in browser
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telegram.me/@MyGovCoronaNewsdesk"))
        }
        return intent!!
    }

    fun whatsApp(context: Context, number: String){
        val url = "https://api.whatsapp.com/send?phone="+number
        try {
            val pm = context.packageManager
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            val i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            startActivity(i)
        } catch (e:PackageManager.NameNotFoundException) {
            Toast.makeText(context, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }
    }

    fun paytmOpen(context: Context){
        val url = "https://p-y.tm/Y2XG-AW"
        try {
            val pm = context.packageManager
            pm.getPackageInfo("net.one97.paytm", PackageManager.GET_ACTIVITIES)
            val i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            startActivity(i)
        } catch (e:PackageManager.NameNotFoundException) {
            Toast.makeText(context, "Paytm app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }
    }

    /** Go to **/

    private fun goToSearch() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivityForResult(intent, RQ_SEARCH)
    }

    private fun goToGlobalCases() {
        val intent = Intent(this, GlobalCasesActivity::class.java)
        intent.putExtra(GlobalCasesActivity.COUNTRY_SELECTED, countrySelected)
        startActivity(intent)
    }

    private fun goToSymptoms() {
        startActivity(Intent(this, SymptomsActivity::class.java))
    }

    private fun goToMedia() {
        startActivity(Intent(this, MediaActivity::class.java))
    }

    private fun goToYoga() {
        startActivity(Intent(this, YogaActivity::class.java))
    }

    private fun goToDonations() {
        startActivity(Intent(this, Donations::class.java))
    }

    private fun goToMyth(value: String) {
        val intent = Intent(this, MythActivity::class.java)
        intent.putExtra("value", value)
        startActivity(intent)
    }

    private fun goToStages(pos: String) {
        val intent = Intent(this, StagesActivity::class.java)
        intent.putExtra("pos", pos)
        startActivity(intent)
    }

    private fun goToWeb(url: String, title: String) {
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra("url", url)
        intent.putExtra("value", "home")
        intent.putExtra("title", title)
        startActivity(intent)
    }

    private fun goToEmergencyGuide() {
        startActivity(Intent(this, EmergencyActivity::class.java))
    }

    private fun goToFeedback() {
        startActivity(Intent(this, FeedbackActivity::class.java))
    }

    private fun goToForms() {
        startActivity(Intent(this, FormsActivity::class.java))
    }

    private fun goToCredits() {
        startActivity(Intent(this, CreditsActivity::class.java))
    }

    private fun goToFAQ() {
        startActivity(Intent(this, FAQActivity::class.java))
    }

    private fun goToHelp() {
        startActivity(Intent(this, HelplineActivity::class.java))
    }

    private fun sendEmail() {
        val camera: RelativeLayout
        val gallery: RelativeLayout
        val dialog = Dialog(this)
        val li =
            this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = li.inflate(R.layout.picselector, null, false)
        val window = dialog.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        v.background = resources.getDrawable(R.drawable.roundalert)
        dialog.setContentView(v)
        camera = dialog.findViewById(R.id.camera)
        gallery = dialog.findViewById(R.id.mail)
        dialog.show()
        camera.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_DIAL,
                Uri.fromParts("tel", "+919584215603", null)
            )

            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CALL_PHONE),
                    1
                )

            } else {
                startActivity(intent)
                //Toast.makeText(this@HelplineActivity,"Not allowed",Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        gallery.setOnClickListener {
            Toast.makeText(this, getString(R.string.email_message), Toast.LENGTH_LONG).show()
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.type = "message/rfc822"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("c19care@gmail.com"))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Covid Care")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello!")

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."))
            } catch (ex: ActivityNotFoundException) {
                // Empty
            }
            dialog.dismiss()
        }
    }

    private fun openwp() {
        val camera: RelativeLayout
        val gallery: RelativeLayout
        val dialog = Dialog(this)
        val li =
            this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = li.inflate(R.layout.whatselector, null, false)
        val window = dialog.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        v.background = resources.getDrawable(R.drawable.roundalert)
        dialog.setContentView(v)
        camera = dialog.findViewById(R.id.camera)
        gallery = dialog.findViewById(R.id.mail)
        dialog.show()
        camera.setOnClickListener {
            whatsApp(this,"+919013151515")
            dialog.dismiss()
        }
        gallery.setOnClickListener {
            whatsApp(this,"+41225017596")
            dialog.dismiss()
        }
    }

    /** Observers **/

    private fun observerTotalCases() = Observer<TotalCases> {
        titleCasesText.text = getString(R.string.home_bottom_dialog_title)
        confirmedCountText.text = formatNumber(it.confirmed)
        deathCountText.text = formatNumber(it.deaths)
        recoveredCountText.text = formatNumber(it.recovered)
    }

    private fun observerGetCountries() = Observer<MutableList<Country>> {
        val bitmap = changeBitmap(this, R.drawable.ic_marker, 80, 80)
        it.forEach { country ->

            val markerOptions = MarkerOptions()
                .position(LatLng(country.latitude, country.longitude))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))

            val marker = mMap?.addMarker(markerOptions)
            marker?.run { viewModel.data.put(marker, country) }
        }
    }

    companion object {
        private const val ZOOM_MAP = 4.8f

        private const val RQ_ACCESS_FINE_LOCATION = 1
        private const val RQ_SEARCH = 2
    }
}
