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
import androidx.core.net.toUri
import coil.load
import com.example.admin_app.databinding.ActivityEditAboutBinding
import com.example.admin_app.databinding.ActivityEditTeamBinding
import com.example.admin_app.doctor.Data
import com.example.admin_app.doctor.doctor
import com.example.admin_app.doctor.position
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class EditTeamActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditTeamBinding
    var db = Firebase.firestore
    var selectedPhotoUri: Uri? = null
    var img_url = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityEditTeamBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        super.onCreate(savedInstanceState)
        val team_selected = teamlist[position]
        teamlist.removeAt(position)
        val img_view = binding.memimage
        img_url=team_selected.image.toString()
        img_view.load(team_selected.image?.toUri()) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
        db.collection("Team").get()
            .addOnSuccessListener { datas ->
                for (i in datas) {
                    val obj = team(
                        i.data["name"].toString(),
                        i.data["designation"].toString(),
                        i.data["number"].toString(),
                        i.data["about"].toString(),
                        i.data["image"].toString()
                    )
                    binding.memname.setText(i.data["name"].toString())
                    binding.memdesignation.setText(i.data["designation"].toString())
                    binding.memnumber.setText(i.data["number"].toString())
                    binding.memabout.setText(i.data["about"].toString())




                }

            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }
        binding.memsubmit.setOnClickListener {
            var about = binding.memabout.text.toString()
            var number = binding.memdesignation.text.toString()
            var designation = binding.memnumber.text.toString()
            var name = binding.memname.text.toString()

            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Uploading....")
            progressDialog.setCancelable(false)
            progressDialog.show()




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