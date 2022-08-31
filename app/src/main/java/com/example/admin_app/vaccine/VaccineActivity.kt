package com.example.admin_app.vaccine

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_app.PatientProfileActivity
import com.example.admin_app.R
import com.example.admin_app.databinding.ActivityReportsBinding
import com.example.admin_app.databinding.ActivityVaccineBinding
import com.example.admin_app.reports.ReportsAdapter
import com.example.admin_app.reports.addreports
import com.example.admin_app.reports.pdfviewer
import com.example.admin_app.reports.reports
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

//var vaccine_list : ArrayList<vaccine> =  arrayListOf<vaccine>()
//var vaccine_id : String = ""

class VaccineActivity  : AppCompatActivity(){


    private lateinit var binding: ActivityVaccineBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("reports","open")
        binding = ActivityVaccineBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)
        val adapter = ViewPagerAdapter(supportFragmentManager,lifecycle)
        binding.viewpager.adapter =adapter
         TabLayoutMediator(binding.tabLayout,binding.viewpager){ tab, position->
             when(position){
                 0->{
                     tab.text = "Vaccines"
                 }
                 1->{
                     tab.text = "Requests"
                 }
                 2->{
                     tab.text = "Certificates"
                 }
             }

         }.attach()



    }
    override fun onBackPressed() {
        val intent= Intent(this, PatientProfileActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }
}