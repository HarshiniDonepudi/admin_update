package com.example.admin_app

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_app.databinding.ActivityGalleryBinding
import com.example.admin_app.doctor.Doctorslist
import com.example.admin_app.doctor.position
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

var urlimage : String = ""
class GalleryActivity : AppCompatActivity(),ImageAdapter.Click {
    private lateinit var recyclerView: RecyclerView
    private lateinit var photoAdapter: ImageAdapter
    private lateinit var binding: ActivityGalleryBinding
    var db = Firebase.firestore

    private var dataList = ArrayList<image>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 2)
        binding.pic.setOnClickListener {
            val intent = Intent(this, AddPicActivity::class.java)
            startActivity(intent)


        }

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        if (!Doctorslist().isConnected(applicationContext)) {
            Toast.makeText(applicationContext, "No Internet ", Toast.LENGTH_SHORT).show();
            Log.e("network--->","if-block")
            progressDialog.dismiss()
        }
        db.collection("Gallery").get()
            .addOnSuccessListener { datas ->
                for (i in datas) {
                    Log.e("list", "${i}")
                    val obj = image(
                        i.data["image"].toString(),
                        i.data["text"].toString(),
                    )

                    dataList.add(obj)
                }
                photoAdapter = ImageAdapter(dataList,this@GalleryActivity)
                recyclerView.adapter = photoAdapter
                progressDialog.dismiss()


            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }

    }

    override fun go_to_main_view(url: String,text:String) {

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Do you want to View or Delete?")
            .setCancelable(true)
        dialogBuilder.setPositiveButton("View") { dialog, id ->
            urlimage = url
            val intent = Intent(this,ImageActivity::class.java)
            startActivity(intent)
        }
        dialogBuilder.setNegativeButton("Delete") { dialog, id ->
            val ref = FirebaseStorage.getInstance().getReference("/Gallery/$text")
            ref.delete().addOnSuccessListener { Log.e("Deleting","---> img Deleted") }
                .addOnFailureListener{Log.e("Deleting","---> img not deleted")}
            db.collection("Gallery").document(text)
                .delete().addOnSuccessListener {
                    Toast.makeText(this, "Deleting entry", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener {
                    Toast.makeText(
                        this, "" +
                                "Failed to delete", Toast.LENGTH_SHORT
                    ).show()
                }
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }

        val alert = dialogBuilder.create()
        alert.show()


    }
    override fun onBackPressed() {
        val intent= Intent(this,OthersActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }
    }

