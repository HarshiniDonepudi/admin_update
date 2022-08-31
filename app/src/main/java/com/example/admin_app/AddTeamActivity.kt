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
import com.example.admin_app.databinding.ActivityAddTeamBinding
import com.example.admin_app.vaccine.VaccineActivity
import com.example.admin_app.vaccine.newvaccine
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class AddTeamActivity : AppCompatActivity()     {
private lateinit var binding: ActivityAddTeamBinding
var db = Firebase.firestore
    var selectedPhotoUri: Uri? = null
    var img_url = ""




override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAddTeamBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)
    binding.selectmempic.setOnClickListener{
        val intent = Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent,0)


    }
    binding.memsubmit.setOnClickListener {
        val about = binding.memabout.text.toString()
        val name = binding.memname.text.toString()
        val designation = binding.memdesignation.text.toString()
        val number = binding.memnumber.text.toString()



        if (selectedPhotoUri != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Uploading....")
            progressDialog.setCancelable(false)
            progressDialog.show()

            val ref = FirebaseStorage.getInstance().getReference("/Team/$name")
            val bitmap =
                MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedPhotoUri)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
            val reducedImage: ByteArray = byteArrayOutputStream.toByteArray()

            ref.putBytes(reducedImage)
                .addOnSuccessListener {

                    ref.downloadUrl.addOnSuccessListener {
                        img_url = it.toString()

                        val data = team(
                           name,
                            designation,
                            number,
                            about,
                            img_url,

                        )
                        val diagnosis = db.collection("Team").document(name).set(data)
                            .addOnSuccessListener {
                                Log.e("success", "${data}")
                                val toast =
                                    Toast.makeText(
                                        this,
                                        " Sucessfully added",
                                        Toast.LENGTH_SHORT
                                    )
                                toast.show()
                                val intent = Intent(this, OurTeamActivity::class.java)
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
            binding.memimage.setBackgroundDrawable(bitmapDrawable)
        }
    }
    override fun onBackPressed() {
        val intent= Intent(this,OurTeamActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }
}