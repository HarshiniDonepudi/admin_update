package com.example.admin_app.reports

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_app.PatientProfileActivity
import com.example.admin_app.PresActivity
import com.example.admin_app.R
import com.example.admin_app.databinding.ActivityDoctorslistBinding
import com.example.admin_app.databinding.ActivityReportsBinding
import com.example.admin_app.doctor.*
import com.example.admin_app.vaccine.VaccineActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

var reportslist : ArrayList<reports> =  arrayListOf<reports>()
var report_id : String = ""
class ReportsActivity : AppCompatActivity(),ReportsAdapter.Click {
    private lateinit var recyclerView: RecyclerView
    val db = FirebaseFirestore.getInstance()

    private lateinit var binding: ActivityReportsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("reports","open")
        binding = ActivityReportsBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        binding.addpres.setOnClickListener {
            val intent = Intent(this, addreports::class.java)
            startActivity(intent)
        }
        recyclerView = binding.RecyclerView
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        reportslist=arrayListOf<reports>()
        if (!Doctorslist().isConnected(applicationContext)) {
            Toast.makeText(applicationContext, "No Internet ", Toast.LENGTH_SHORT).show();
            Log.e("network--->","if-block")
            progressDialog.dismiss()
        }
        db.collection("Reports")
            .get()
            .addOnSuccessListener {
                    datas->

                for ( i in datas){
                    Log.e("list","${i}")
                    val obj = reports(
                        i.data["report_id"].toString(),
                        i.data["pemial"].toString(),
                        i.data["pname"].toString(),
                        i.data["dname"].toString(),
                        i.data["date"].toString(),
                        i.data["did"].toString(),
                        i.data["pdf"].toString()
                    )
                    reportslist.add(obj)
                }
                recyclerView.adapter = ReportsAdapter(
                    reportslist,this@ReportsActivity,this@ReportsActivity)
                progressDialog.dismiss()
                Log.e("list", "$reportslist")
            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }
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

    fun filter(text: String) {
        val filtered: ArrayList<reports> = ArrayList()

        val courseAry: ArrayList<reports> = reportslist

        for (eachCourse in courseAry) {
            if (eachCourse.date!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.report_id!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            } else if (eachCourse.report_id!!.toLowerCase()
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
            recyclerView.adapter = ReportsAdapter(
                filtered,this@ReportsActivity,this@ReportsActivity) }
    }
    override fun go_to_main_view(url: String, r_id: String) {
        pdfviewer().seturl(url)
        report_id=r_id
        val intent = Intent(this, pdfviewer::class.java)
        startActivity(intent)

    }

    override fun go_to_main_delete(rid: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Do you want delete?")
            .setCancelable(true)
        dialogBuilder.setPositiveButton("Cancel"){
                dialog,id->
        }
        dialogBuilder.setNegativeButton("Delete"){
                dialog,id->
//            val Id= reports[position].id
//            val url = doctor[position].img_url
            val ref = FirebaseStorage.getInstance().getReference("/reports/$rid")
            ref.delete().addOnSuccessListener { Log.e("Deleting","---> img Deleted") }
                .addOnFailureListener{Log.e("Deleting","---> img not deleted")}
            db.collection("Reports").document(rid.toString())
                .delete().addOnSuccessListener{
                Toast.makeText(this,"Deleting entry", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener{
                Toast.makeText(this,"" +
                        "Failed to delete", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this, ReportsActivity::class.java)
            startActivity(intent)


        }

        val alert = dialogBuilder.create()
        alert.show()
    }
    override fun onBackPressed() {
        val intent= Intent(this, PatientProfileActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }
}