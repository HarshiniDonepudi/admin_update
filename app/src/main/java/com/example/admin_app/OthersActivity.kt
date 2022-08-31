package com.example.admin_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.admin_app.doctor.Doctorslist

class OthersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_others)
        val gallery: Button = findViewById(R.id.gallery)
        val facilities: Button = findViewById(R.id.facilities)
        val about: Button = findViewById(R.id.about)
        val contact: Button = findViewById(R.id.contact)
        val news: Button = findViewById(R.id.news)
        gallery.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }
        about.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
       facilities.setOnClickListener {
            val intent = Intent(this, FacilitiesActivity::class.java)
            startActivity(intent)
        }
        contact.setOnClickListener {
            val intent = Intent(this, OurTeamActivity::class.java)
            startActivity(intent)
        }
        news.setOnClickListener {
            val intent = Intent(this, LCGHMainActivity::class.java)
            startActivity(intent)
        }

    }
    override fun onBackPressed() {
        val intent= Intent(this,DandPActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }
}