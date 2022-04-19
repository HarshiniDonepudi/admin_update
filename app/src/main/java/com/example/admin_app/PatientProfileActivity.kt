package com.example.admin_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.admin_app.databinding.ActivityMainBinding
import com.example.admin_app.databinding.ActivityPatientProfileBinding

class PatientProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientProfileBinding.inflate(layoutInflater)
        val view = binding.root
        binding.vaccine.setOnClickListener{
            val intent = Intent(this, VaccineActivity::class.java)
            startActivity(intent)
        }
        binding.results.setOnClickListener{
            val intent = Intent(this, ReportsActivity::class.java)
            startActivity(intent)
        }
        binding.history.setOnClickListener{
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
        binding.diagnosis.setOnClickListener{
            val intent = Intent(this, DiagnosisActivity::class.java)
            startActivity(intent)
        }
        binding.medicine.setOnClickListener{
            val intent = Intent(this, MedicinesActivity::class.java)
            startActivity(intent)
        }
    }
}