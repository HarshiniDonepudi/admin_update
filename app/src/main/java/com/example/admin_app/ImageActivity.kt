package com.example.admin_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import coil.load
import com.example.admin_app.doctor.position
import com.example.admin_app.reports.ReportsActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ImageActivity : AppCompatActivity(){
override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_zoom_class)
    val prescription: ImageView = findViewById(R.id.largeImage)
    prescription.load(urlimage.toUri()) {
        placeholder(R.drawable.loading_animation)
        error(R.drawable.ic_broken_image)
    }
}
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.photo_menu, menu)


        return super.onCreateOptionsMenu(menu)


    }
}
