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
import coil.clear
import com.example.admin_app.databinding.ActivityDiagnosisBinding
import com.example.admin_app.databinding.ActivityEditDiagnosisBinding
import com.example.admin_app.doctor.Doctorslist
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class EditDiagnosisActivity : AppCompatActivity() {
    var db = Firebase.firestore
    var selectedPhotoUri: Uri? = null
    var img_url = ""
    private lateinit var binding: ActivityEditDiagnosisBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditDiagnosisBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        binding.img.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)

        }

        binding.button.setOnClickListener {
            var date = binding.date.text.toString()
            var rid = binding.rId.text.toString()
            var pemail = binding.pEmail.text.toString()
            var pname = binding.pName.text.toString()
            var did = binding.dId.text.toString()
            var dname = binding.dName.text.toString()

            if (selectedPhotoUri != null) {
                val progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Uploading....")
                progressDialog.setCancelable(false)
                progressDialog.show()

                val ref = FirebaseStorage.getInstance().getReference("/prescription/$rid")
                val bitmap =
                    MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedPhotoUri)
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
                val reducedImage: ByteArray = byteArrayOutputStream.toByteArray()

                ref.putBytes(reducedImage)
                    .addOnSuccessListener {

                        ref.downloadUrl.addOnSuccessListener {
                            img_url = it.toString()

                            val data = diagnosis(
                                rid,
                                pemail,
                                pname,
                                dname,
                                date,
                                did,
                                img_url
                            )
                            val diagnosis = db.collection("Diagnosis").document(rid).set(data)
                                .addOnSuccessListener {
                                    Log.e("success", "${data}")
                                    val toast =
                                        Toast.makeText(
                                            this,
                                            " Sucessfully added",
                                            Toast.LENGTH_SHORT
                                        )
                                    toast.show()
                                    val intent = Intent(this, DiagnosisActivity::class.java)
                                    startActivity(intent)
                                }

                        }.addOnFailureListener { e ->
                            val toast = Toast.makeText(this, "failed", Toast.LENGTH_SHORT)
                            toast.show()
                            Log.d("Uploading", "Failed ")
                        }


                    }
            }
        }
    }




            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
                if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
//            binding.img.clear()
                    selectedPhotoUri = data.data
//            selectedPhotoUri?.let { doCrop(it) }
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
                    val bitmapDrawable = BitmapDrawable(bitmap)
                    binding.img.setBackgroundDrawable(bitmapDrawable)
                }
            }
        }





