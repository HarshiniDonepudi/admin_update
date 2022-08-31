package com.example.admin_app

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.admin_app.databinding.ActivityConfirmVaccineBinding
import com.example.admin_app.databinding.ActivityEditAboutBinding
import com.example.admin_app.vaccine.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
  var requestList = ArrayList<bookvaccine>()
class ConfirmVaccineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmVaccineBinding
    var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityConfirmVaccineBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        super.onCreate(savedInstanceState)

        binding.aboutsubmit.setOnClickListener {
            var status = binding.status.text.toString()

            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Uploading....")
            progressDialog.setCancelable(false)
            progressDialog.show()




            val diagnosis = db.collection("VaccineRequests").document(vac).update("status", status)
                .addOnSuccessListener {

                    val toast =
                        Toast.makeText(
                            this,
                            " Sucessfully added",
                            Toast.LENGTH_SHORT
                        )
                    toast.show()
                    val intent = Intent(this, VaccineActivity::class.java)
                    startActivity(intent)
                }

                .addOnFailureListener { e ->
                    val toast = Toast.makeText(this, "failed", Toast.LENGTH_SHORT)
                    toast.show()
                    Log.d("Uploading", "Failed ")
                }


        }
    }

}