package com.example.admin_app

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.admin_app.databinding.ActivityEditAboutBinding
import com.example.admin_app.databinding.ActivityEditFacilitiesBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class EditFacilitiesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditFacilitiesBinding
    var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityEditFacilitiesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        super.onCreate(savedInstanceState)
        db.collection("facilities").whereEqualTo("fac_id" ,textid).get()
            .addOnSuccessListener { datas ->
                for (i in datas) {
                    val obj = facilities(
                        i.data["fac_id"].toString(),
                        i.data["text"].toString(),
                        i.data["date"].toString(),
                    )
                    binding.facilities.setText(i.data["text"].toString())

                }

            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }
        binding.fsubmit.setOnClickListener {
            var fac = binding.facilities.text.toString()
            val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
            val date = sdf.format(java.util.Date())
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Uploading....")
            progressDialog.setCancelable(false)
            progressDialog.show()




            val data = facilities(
                textid,
                fac,
                date

            )
            val diagnosis = db.collection("facilities").document(textid).update("text",fac)
                .addOnSuccessListener {
                    Log.e("success", "${data}")
                    val toast =
                        Toast.makeText(
                            this,
                            " Sucessfully added",
                            Toast.LENGTH_SHORT
                        )
                    progressDialog.dismiss()
                    toast.show()
                    val intent = Intent(this, FacilitiesActivity::class.java)
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

}
