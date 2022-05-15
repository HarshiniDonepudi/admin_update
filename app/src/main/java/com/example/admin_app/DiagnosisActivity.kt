package com.example.admin_app

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_app.databinding.ActivityAppointmentBinding
import com.example.admin_app.databinding.ActivityDiagnosisBinding
import com.example.admin_app.doctor.Data
import com.example.admin_app.doctor.MainActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

lateinit  var diagnosislist : ArrayList<diagnosis>
var urlimg : String = ""

class DiagnosisActivity : AppCompatActivity(),DiagnosisAdapter.Click {
    var db = Firebase.firestore
    private lateinit var recyclerView: RecyclerView

    private lateinit var binding: ActivityDiagnosisBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDiagnosisBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)
        recyclerView = binding.RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        Log.e("dfdf", "dfdsfs")
        binding.addpres.setOnClickListener{
//            db = FirebaseFirestore.getInstance()
//           val data=Data(1254646,"name",
//            "spec",
//            "about",
//            "not done yet",
//            "exp",
//            avl as ArrayList<Int>,
//            slots,
//            "lan"
//            )
//            Log.e("data", "$id ,$name , $about , $exp ,$lan")
//            dbref.child(data.id.toString()).setValue(data)
            val intent = Intent(this, EditDiagnosisActivity::class.java)
            startActivity(intent)
        }



        retrive()

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                filter(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun retrive() {
        diagnosislist = arrayListOf<diagnosis>()
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        db.collection("Diagnosis").get()
            .addOnSuccessListener { datas ->
                for (i in datas) {
                    Log.e("list", "${i}")
                    val obj = diagnosis(
                        i.data["r_id"].toString(),
                        i.data["pemial"].toString(),
                        i.data["pname"].toString(),
                        i.data["dname"].toString(),
                        i.data["date"].toString(),
                        i.data["did"].toString(),
                        i.data["img"].toString()
                    )

                    diagnosislist.add(obj)
                }
                recyclerView.adapter = DiagnosisAdapter(diagnosislist,this@DiagnosisActivity)
                progressDialog.dismiss()
                Log.e("list", "$diagnosislist")
            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }

    }

    fun filter(text: String) {
        val filtered: ArrayList<diagnosis> = ArrayList()

        val courseAry: ArrayList<diagnosis> = diagnosislist

        for (eachCourse in courseAry) {
            if (eachCourse.date!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.r_id!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            } else if (eachCourse.r_id!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.date!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            } else if (eachCourse.did!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.did!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            } else if (eachCourse.pname!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.pname!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
//            } else if (eachCourse.appointment_id!!.toLowerCase()
//                    .contains(text.toLowerCase()) || eachCourse.spec!!.toLowerCase()
//                    .contains(text.toLowerCase())
//            ) {
//                filtered.add(eachCourse)
//            }
            }

            //calling a method of the adapter class and passing the filtered list
//        DoctorsAdapter(this,filtered)
            recyclerView.adapter = DiagnosisAdapter(filtered,this@DiagnosisActivity)
        }
    }

    override fun go_to_main_view(url: String) {
        urlimg=url
        val intent = Intent(this, PresActivity::class.java)
        startActivity(intent)
    }
}