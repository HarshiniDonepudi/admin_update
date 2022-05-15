package com.example.admin_app

import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_app.databinding.ActivityAppointmentBinding
import com.example.admin_app.databinding.ActivityMainBinding
import com.example.admin_app.doctor.DoctorAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

lateinit  var appointmentlist : ArrayList<appointment>
class AppointmentActivity : AppCompatActivity(), AppointmentAdapter.Click  {
    val db = Firebase.firestore
    private lateinit var recyclerView: RecyclerView

    private lateinit var binding: ActivityAppointmentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)
        recyclerView = binding.RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        Log.e("dfdf","dfdsfs")



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
        appointmentlist = arrayListOf<appointment>()
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        db.collection("Appointment").get()
            .addOnSuccessListener { datas ->
                for (i in datas) {
                    Log.e("list", "${i}")
                    val obj = appointment(
                        i.data["appointment_id"].toString(),
                        i.data["name"].toString(),
                        i.data["spec"].toString(),
                        i.data["patient_emial"].toString(),
                        i.data["date"].toString(),
                        i.data["timeslot"].toString(),
                        i.data["status"].toString()
                    )
                    appointmentlist.add(obj)
                }
                recyclerView.adapter=AppointmentAdapter(appointmentlist,this@AppointmentActivity,this@AppointmentActivity)
                progressDialog.dismiss()
                Log.e("list", "$appointmentlist")
            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }

    }

    fun filter(text: String) {
        val filtered: ArrayList<appointment> = ArrayList()

        val courseAry: ArrayList<appointment> = appointmentlist

        for (eachCourse in courseAry) {
            if (eachCourse.date!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.spec!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            } else if (eachCourse.name!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.spec!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            } else if (eachCourse.status!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.spec!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            } else if (eachCourse.timeslot!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.spec!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            } else if (eachCourse.appointment_id!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.spec!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            }
        }

        //calling a method of the adapter class and passing the filtered list
//        DoctorsAdapter(this,filtered)
        recyclerView.adapter = AppointmentAdapter(filtered,this@AppointmentActivity,this@AppointmentActivity)
    }

    override fun go_to_main_approved(R_id: Int) {
        Log.e("approved","Clicled")
        db.collection("Appointment").document(appointmentlist[R_id].appointment_id.toString()).update("status","approved").
                addOnSuccessListener { Log.e("approved","changed")
                retrive()}
            .addOnFailureListener{
                Log.e("approved","faild")
            }


    }

    override fun go_to_main_disapproved(R_id: Int) {
        Log.e("disapproved","Clicled")
        db.collection("Appointment").document(appointmentlist[R_id].appointment_id.toString()).update("status","disapproved")

            .addOnSuccessListener { Log.e("approved","changed")
            retrive()}
            .addOnFailureListener{
                Log.e("approved","faild")
            }
    }
}




