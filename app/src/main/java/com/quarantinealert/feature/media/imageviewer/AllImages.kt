package com.quarantinealert.feature.media.imageviewer

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.quarantinealert.R
import com.quarantinealert.feature.media.adapter.PhotosAdapter
import com.quarantinealert.firebase.fcm.CovidMessagingService
import com.quarantinealert.model.RetrieveModel
import com.quarantinealert.model.UriModel
import com.quarantinealert.util.MySingleton
import com.quarantinealert.util.VolleySingleton
import kotlinx.android.synthetic.main.activity_symptoms.toolbar
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.GET


class AllImages : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var preferences: SharedPreferences
    lateinit var pager: ViewPager
    lateinit var title: TextView
    lateinit var progressDialog: ProgressDialog
    lateinit var photosAdapter: PhotosAdapter
    var uploads: ArrayList<RetrieveModel> = arrayListOf()
    var PICK_IMAGE_REQUEST = 234
    lateinit var mSTRef: StorageReference
    lateinit var mStorageReference: StorageReference
    lateinit var mDBRef: DatabaseReference
    lateinit var addPhotos: MaterialButton
    var uriArray: ArrayList<Uri> = arrayListOf()
    var savedImageArray: MutableList<UriModel> = mutableListOf()
    var counter: Int = 0
    var value: String = ""
    var singleUri: String = ""
    var itemsProcessed = 0
    lateinit var nothingFound: View
    lateinit var loadView: ImageView
    val FCM_API = "https://fcm.googleapis.com/fcm/send"
    val serverKey = "key=" + "AAAAPLUMcW8:APA91bH0833fN6Far3M-6N9gXmCOZxem9zvGAVIQN6wPDkmUG_mfuN2POYAwoQmXaMwf463NVQWYJ3Sw_nwMAAz4Dy9ofDDyeVUoPr2NbYTukk5C2ghxS-DGdTxyBvB6f-gY4Yu_SoCw"
    val contentType = "application/json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allimages)
        setOnClick()
        FirebaseMessaging.getInstance().subscribeToTopic("uploads")
        recyclerView = findViewById(R.id.imageRview)
        nothingFound = findViewById(R.id.nothingFound)
        loadView = findViewById(R.id.loadView)
        Glide.with(this)
            .load(R.raw.nothing)
            .into(loadView)
        nothingFound.visibility = View.GONE
        addPhotos = findViewById<MaterialButton>(R.id.addPhotos)
        addPhotos.setOnClickListener(View.OnClickListener {
            showFileChooser()
        })
        value = intent.getStringExtra("value")!!
        if (value.equals("tips")) {
            mSTRef = FirebaseStorage.getInstance().getReference("/Images/MyGov/tips")
            mDBRef = FirebaseDatabase.getInstance().getReference("/Images/MyGov/tips")
            nothingFound.visibility = View.GONE
        } else if (value.equals("social")) {
            mSTRef = FirebaseStorage.getInstance().getReference("/Images/Social")
            mDBRef = FirebaseDatabase.getInstance().getReference("/Images/Social")
        } else if (value.equals("volunteers")) {
            mSTRef = FirebaseStorage.getInstance().getReference("/Images/Volunteers")
            mDBRef = FirebaseDatabase.getInstance().getReference("/Images/Volunteers")
        } else if (value.equals("updates")) {
            mSTRef = FirebaseStorage.getInstance().getReference("/Images/MyGov/updates")
            mDBRef = FirebaseDatabase.getInstance().getReference("/Images/MyGov/updates")
        } else {
            nothingFound.visibility = View.GONE
        }
        recyclerView.setHasFixedSize(true)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.setLayoutManager(staggeredGridLayoutManager)
        photosAdapter = PhotosAdapter(savedImageArray)
        recyclerView.setAdapter(photosAdapter)
        val recyclerViewState: Parcelable
        recyclerViewState = recyclerView.getLayoutManager()?.onSaveInstanceState()!!
        recyclerView.getLayoutManager()?.onRestoreInstanceState(recyclerViewState)
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Fetching photos...")
        progressDialog.show()
        uploads.clear()
        getImages()

    }

    private fun getImages() {
        savedImageArray.clear()
        mSTRef.listAll().addOnSuccessListener { listResult ->
            listResult.prefixes.forEach { prefix ->
                Log.e("getFail4", prefix.toString())
            }
            listResult.items.forEach { item ->
                item.downloadUrl.addOnSuccessListener { uri ->
                    savedImageArray.add(UriModel(uri.toString()))
                    Log.e("url", uri.toString())
                    photosAdapter.notifyDataSetChanged()
                    photosAdapter.setItemClickListener(object : PhotosAdapter.ItemClickListener {
                        override fun onItemClick(view: View, position: Int, postId: String) {
                            val intent = Intent(this@AllImages, ImageActivty::class.java)
                            intent.putExtra("imagepath", postId)
                            intent.putExtra("pagerpos", position)
                            startActivity(intent)
                            Log.e("rdata", postId+" "+position)
                        }
                    })
                }
            }
            progressDialog.dismiss()
        }
            .addOnFailureListener {
                Log.e("getFail6", "error")
                progressDialog.dismiss()
            }
    }

    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        uriArray.clear()
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            progressDialog.setCancelable(false)
            progressDialog.setTitle("Please wait")
            progressDialog.setMax(100)
            progressDialog.setMessage("Uploading...")
            if (progressDialog.isShowing()) {
                progressDialog.dismiss()
            } else {
                progressDialog.show()
            }
            if (data!!.data != null) {
                val mImageUri = data.data
                singleUri = mImageUri.toString()
                Toast.makeText(this, "Choose atleast 2 images", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            } else if (data.clipData != null) {
                val mData = data.clipData
                val count = mData!!.itemCount
                for (i in 0 until count) {
                    val item = mData.getItemAt(i).uri
                    uriArray.add(item!!)
                    // Log.e("checkall",getPathFromURI(item))
                }
                uploadImage()
            }
            // Log.e("arrayz", filePathArray.toString())
        }
    }

    private fun uploadImage() {
        for (i in 0 until uriArray.size) {
            mStorageReference = mSTRef.child(getFileName(uriArray.get(i)))
            mStorageReference.putFile(uriArray.get(i)).continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                mStorageReference.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val retrieveModel =
                        RetrieveModel(getFileName(uriArray.get(i)), downloadUri.toString())
                    val upID = mDBRef.push().key
                    mDBRef.child(upID!!).setValue(retrieveModel)
                    progressDialog.dismiss()
                  //  sendNoti(""+uriArray.size+" new images added")
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(getIntent());
                    overridePendingTransition(0, 0)
                } else {
                    progressDialog.dismiss()
                    Log.e("upfail", "Upload Error")
                }
            }
        }
    }

    fun sendNoti(msg:String){
        val TOPIC = mDBRef
        val NOTIFICATION_TITLE = "Data added"
        val NOTIFICATION_MESSAGE = msg
         val notification = JSONObject()
                val notifcationBody = JSONObject()
                try {
                    notifcationBody.put("title", NOTIFICATION_TITLE)
                    notifcationBody.put("message", NOTIFICATION_MESSAGE)

                    notification.put("to", TOPIC)
                    notification.put("data", notifcationBody)
                } catch (e: JSONException) {
                    Log.e("checkerro", "onCreate: " + e.message )
                }
                sendNotification(notification)
    }

    @Override
    private fun sendNotification(notification:JSONObject){


        val stringRequest = object : JsonObjectRequest(Method.POST,FCM_API,notification,
            com.android.volley.Response.Listener<JSONObject> { response ->
                try {
                    Toast.makeText(this,"Notification sent ",Toast.LENGTH_SHORT).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            object : com.android.volley.Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    Toast.makeText(this@AllImages, "Request error "+volleyError.message, Toast.LENGTH_LONG).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("Authorization", serverKey)
                params.put("Content-Type", contentType)
                return params
            }
        }

        //adding request to queue
        MySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }




    fun <T> removeDuplicates(list: ArrayList<T>): ArrayList<T>? {
        val newList = ArrayList<T>()
        // Traverse through the first list
        for (element in list) { // If this element is not present in newList
            if (!newList.contains(element)) {
                newList.add(element)
            }
        }
        return newList
    }

    private fun getFileName(uri: Uri): String {
        var result: String = ""
        if (uri.scheme.equals("content")) {
            val cursor: Cursor = contentResolver.query(uri, null, null, null, null)!!
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor.close()
            }
        }
        if (result == null) {
            result = uri.path!!
            val cut: Int = result.lastIndexOf("/")
            if (cut != null) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    private fun setOnClick() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
