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
import com.example.admin_app.databinding.ActivityAddFacilitiesBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddFacilitiesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddFacilitiesBinding
    var db = Firebase.firestore
    var selectedPhotoUri: Uri? = null
    var img_url = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFacilitiesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.fsubmit.setOnClickListener {
            var about = binding.facilities.text.toString()
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




            val data = facilities(
                newid,
                about,
                date

            )
            val diagnosis = db.collection("facilities").document(newid).set(data)
                .addOnSuccessListener {
                    Log.e("success", "${data}")
                    val toast =
                        Toast.makeText(
                            this,
                            " Sucessfully added",
                            Toast.LENGTH_SHORT
                        )
                    toast.show()
                    val intent = Intent(this, FacilitiesActivity::class.java)
                    startActivity(intent)
                }

                .addOnFailureListener { e ->
                    val toast = Toast.makeText(this, "failed", Toast.LENGTH_SHORT)
                    toast.show()
                    Log.d("Uploading", "Failed ")
                }


        }
        binding.selectpic.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        binding.pfsubmit.setOnClickListener {

            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val date = sdf.format(java.util.Date())

            if (selectedPhotoUri != null) {
                val progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Uploading....")
                progressDialog.setCancelable(false)
                progressDialog.show()

                val ref = FirebaseStorage.getInstance().getReference("/facimage/$date")
                val bitmap =
                    MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedPhotoUri)
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
                val reducedImage: ByteArray = byteArrayOutputStream.toByteArray()

                ref.putBytes(reducedImage)
                    .addOnSuccessListener {

                        ref.downloadUrl.addOnSuccessListener {
                            img_url = it.toString()

                            val data = SlideModel(
                                img_url,
                                date


                            )
                            val diagnosis = db.collection("facimage").document().set(data)
                                .addOnSuccessListener {
                                    Log.e("success", "${data}")
                                    val toast =
                                        Toast.makeText(
                                            this,
                                            " Sucessfully added",
                                            Toast.LENGTH_SHORT
                                        )
                                    toast.show()
                                    val intent = Intent(this, FacilitiesActivity::class.java)
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
            binding.image.setBackgroundDrawable(bitmapDrawable)
        }
    }
    override fun onBackPressed() {
        val intent= Intent(this,FacilitiesActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }
}


