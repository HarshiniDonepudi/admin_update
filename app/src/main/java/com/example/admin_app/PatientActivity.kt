package com.example.admin_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

class PatientActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var patientArrayList: ArrayList<patientdata>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient)
        recyclerView=findViewById(R.id.RecyclerView)
        recyclerView.adapter=PatientAdapter(this)
    }
}