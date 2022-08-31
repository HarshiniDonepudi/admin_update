package com.example.admin_app

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.admin_app.databinding.ActivityAddAboutBinding
import com.example.admin_app.databinding.ActivityNewVaccineBinding
import com.example.admin_app.vaccine.VaccineActivity
import com.example.admin_app.vaccine.newvaccine
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

var id = "about"
class AddAboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAboutBinding
    var db = Firebase.firestore




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAboutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.aboutsubmit.setOnClickListener {
            var about = binding.about.text.toString()
            val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
            val date = sdf.format(java.util.Date())

            val currentDate = sdf.format(Date())
            var newid : String =""
            newid=""+currentDate.subSequence(0,2)+currentDate.subSequence(3,5)+currentDate.subSequence(6,10)+currentDate.subSequence(11,13)+currentDate.subSequence(14,16)+currentDate.subSequence(17,18).toString()
            Log.e("newid", "${newid}")
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Uploading....")
            progressDialog.setCancelable(false)
            progressDialog.show()




                            val data = about(
                                newid,
                              about,
                                date

                            )
                            val diagnosis = db.collection("about").document(newid).set(data)
                                .addOnSuccessListener {
                                    Log.e("success", "${data}")
                                    val toast =
                                        Toast.makeText(
                                            this,
                                            " Sucessfully added",
                                            Toast.LENGTH_SHORT
                                        )
                                    toast.show()
                                    val intent = Intent(this, AboutActivity::class.java)
                                    startActivity(intent)
                                }

                        .addOnFailureListener { e ->
                            val toast = Toast.makeText(this, "failed", Toast.LENGTH_SHORT)
                            toast.show()
                            Log.d("Uploading", "Failed ")
                        }


                    }
            }
    override fun onBackPressed() {
        val intent= Intent(this,AboutActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }





}