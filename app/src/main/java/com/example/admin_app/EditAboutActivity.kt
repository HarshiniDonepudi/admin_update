package com.example.admin_app

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import coil.load
import com.example.admin_app.databinding.ActivityAboutBinding
import com.example.admin_app.databinding.ActivityEditAboutBinding
import com.example.admin_app.doctor.Data
import com.example.admin_app.doctor.doctor
import com.example.admin_app.doctor.position
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class EditAboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditAboutBinding
    var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityEditAboutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        super.onCreate(savedInstanceState)
        db.collection("about").whereEqualTo("about_id", aboutid).get()
            .addOnSuccessListener { datas ->
                for (i in datas) {
                    val obj = about(
                        i.data["about"].toString(),
                        i.data["text"].toString(),
                        i.data["date"].toString(),
                        )
                    binding.about.setText(i.data["text"].toString())

                }

            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }
        binding.aboutsubmit.setOnClickListener {
            var about = binding.about.text.toString()
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val date = sdf.format(java.util.Date())
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Uploading....")
            progressDialog.setCancelable(false)
            progressDialog.show()




            val data = about(
                aboutid,
                about,
                date

            )
            val diagnosis = db.collection("about").document(aboutid).update("text",about)
                .addOnSuccessListener {
                    Log.e("success", "${data}")
                    val toast =
                        Toast.makeText(
                            this,
                            " Sucessfully added",
                            Toast.LENGTH_SHORT
                        )
                    toast.show()
                    progressDialog.dismiss()
                    val intent = Intent(this, AboutActivity::class.java)
                    startActivity(intent)
                }

                .addOnFailureListener { e ->
                    val toast = Toast.makeText(this, "failed", Toast.LENGTH_SHORT)
                    toast.show()
                    progressDialog.dismiss()
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
