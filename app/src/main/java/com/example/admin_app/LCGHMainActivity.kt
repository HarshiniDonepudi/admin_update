package com.example.admin_app

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_app.databinding.ActivityLcghmainBinding
import com.example.admin_app.doctor.Doctorslist
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LCGHMainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var photoAdapter: LCGHMainAdapter
    private lateinit var binding: ActivityLcghmainBinding
    var db = Firebase.firestore

    private var dataList = ArrayList<lcghnews>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLcghmainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 2)
        binding.pic.setOnClickListener {
            val intent = Intent(this, LCGHNewsActivity::class.java)
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
        db.collection("LCGHNews").orderBy("date", Query.Direction.DESCENDING).get()
            .addOnSuccessListener {
                if(!it.isEmpty){
                    for(data in it.documents){
                        val news : lcghnews? = data.toObject(lcghnews::class.java)
                        if(news!= null){
                            dataList.add(news)
                        }
                    }
                    photoAdapter = LCGHMainAdapter(dataList,this@LCGHMainActivity)
                    recyclerView.adapter = photoAdapter
                    progressDialog.dismiss()
                }


            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }

    }


    fun go_to_main_view(image: String) {

    }

    override fun onBackPressed() {
        val intent= Intent(this,OthersActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }
}


