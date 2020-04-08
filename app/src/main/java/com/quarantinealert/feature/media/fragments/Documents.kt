package com.quarantinealert.feature.media.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.quarantinealert.R
import com.quarantinealert.feature.media.adapter.DocumentsAdapter
import com.quarantinealert.feature.webview.WebActivity
import com.quarantinealert.model.PdfModel
import org.apache.commons.io.FilenameUtils
import java.net.URL

class Documents : Fragment() {

    lateinit var mSTRef: StorageReference
    lateinit var mDBRef: DatabaseReference
    lateinit var documentsAdapter:DocumentsAdapter
    var uriArray: MutableList<PdfModel> = mutableListOf()
    var nameArray: MutableList<String> = mutableListOf()
    lateinit var progressDialog: ProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root =  inflater.inflate(R.layout.fragment_documents, container, false)
        mSTRef = FirebaseStorage.getInstance().getReference("/Pdf")
        mDBRef = FirebaseDatabase.getInstance().getReference("/Pdf")
        val rvattachment = root.findViewById<RecyclerView>(R.id.rvattachment)
        rvattachment.setHasFixedSize(true)
        rvattachment.layoutManager = LinearLayoutManager(activity)
        rvattachment.itemAnimator = DefaultItemAnimator()
        documentsAdapter = DocumentsAdapter(uriArray)
        rvattachment.setAdapter(documentsAdapter)
        val recyclerViewState: Parcelable
        recyclerViewState = rvattachment.getLayoutManager()?.onSaveInstanceState()!!
        rvattachment.getLayoutManager()?.onRestoreInstanceState(recyclerViewState)
        progressDialog = ProgressDialog(activity)
        progressDialog.setCancelable(false)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Fetching documents...")
        progressDialog.show()
        getDocs()
        return root
    }

    private fun getDocs() {
        uriArray.clear()
        nameArray.clear()
        mSTRef.listAll().addOnSuccessListener { listResult ->
            listResult.prefixes.forEach { prefix ->
                Log.e("getFail4", prefix.toString())
            }
            listResult.items.forEach { item ->

//                item.metadata.addOnSuccessListener { storageMetadata ->
//                    val filename = storageMetadata.getName()
//                    nameArray.add(filename.toString())
//                    documentsAdapter.notifyDataSetChanged()
//
//                }.addOnFailureListener(OnFailureListener {
//                    Log.e("getFail6", "error")
//                    progressDialog.dismiss()
//                })
                item.downloadUrl.addOnSuccessListener { uri ->
                    val url = URL(uri.toString())
                    val fileName = FilenameUtils.getName(url.getPath())
                    uriArray.add(PdfModel(uri.toString(),fileName))
                  //  Log.e("url", uri.toString())
                    documentsAdapter.notifyDataSetChanged()
                    documentsAdapter.setItemClickListener(object : DocumentsAdapter.OnItemClickListener {
                        override fun onItemClick(hotline: String, get: String) {
                            val intent = Intent(activity, WebActivity::class.java)
                            intent.putExtra("url", hotline)
                            intent.putExtra("value", "pdf")
                            intent.putExtra("title", get)
                            startActivity(intent)
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

    companion object {
        fun newInstance(): Documents = Documents()
    }
}