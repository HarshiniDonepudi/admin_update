package com.example.admin_app

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.admin_app.databinding.ActivityAboutBinding
import com.example.admin_app.databinding.ActivityFacilitiesBinding
import com.example.admin_app.doctor.Doctorslist
import com.example.admin_app.doctor.position
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

lateinit  var facilitiesList : ArrayList<facilities>
lateinit  var imageList : ArrayList<SlideModel>
var textid =""
var posi: Int =-1
class FacilitiesActivity : AppCompatActivity(),FacilitiesAdapter.Click {
    var db = Firebase.firestore
    private lateinit var recyclerView: RecyclerView

    private lateinit var binding: ActivityFacilitiesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityFacilitiesBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        Log.e("dfdf", "dfdsfs")
        binding.pic.setOnClickListener {
//            db = FirebaseFirestore.getInstance()
//           val data=Data(1254646,"name",
//            "spec",
//            "about",
//            "not done yet",
//            "exp",
//            avl as ArrayList<Int>,
//            slots,
//            "lan"
//            )
//            Log.e("data", "$id ,$name , $about , $exp ,$lan")
//            dbref.child(data.id.toString()).setValue(data)
            val intent = Intent(this, AddFacilitiesActivity::class.java)
            startActivity(intent)
        }



        facilitiesList = arrayListOf<facilities>()
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        if (!Doctorslist().isConnected(applicationContext)) {
            Toast.makeText(applicationContext, "No Internet ", Toast.LENGTH_SHORT).show();
            Log.e("network--->","if-block")
            progressDialog.dismiss()
        }
        db.collection("facilities").orderBy("date", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { datas ->
                for (i in datas) {
                    Log.e("list", "${i}")
                    val obj = facilities(
                        i.data["fac_id"].toString(),
                        i.data["text"].toString(),
                        i.data["date"].toString()
                    )

                    facilitiesList.add(obj)

                }
                recyclerView.adapter = FacilitiesAdapter(facilitiesList, this@FacilitiesActivity)
                progressDialog.dismiss()
            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }
        imageList = arrayListOf<SlideModel>()

        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        if (!Doctorslist().isConnected(applicationContext)) {
            Toast.makeText(applicationContext, "No Internet ", Toast.LENGTH_SHORT).show();
            Log.e("network--->","if-block")
            progressDialog.dismiss()
        }
        db.collection("facimage").orderBy("date", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { datas ->
                for (i in datas) {
                    Log.e("list", "${i}")
                    val obj = com.denzcoskun.imageslider.models.SlideModel(
                        i.data["image"].toString(),
                        i.data["date"].toString()
                    )

                    imageList.add(obj)
                    binding.imageslider.setImageList(imageList, ScaleTypes.FIT)

                }
                recyclerView.adapter = FacilitiesAdapter(facilitiesList, this@FacilitiesActivity)
                progressDialog.dismiss()
            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }

    }


    override fun onBackPressed() {
        val intent = Intent(this, OthersActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }


    override fun go_to_main_view(text: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Do you want to Edit or Delete?")
            .setCancelable(true)
        dialogBuilder.setPositiveButton("Edit") { dialog, id ->
            textid = text
            posi = id
            val intent = Intent(this, EditFacilitiesActivity::class.java)
            startActivity(intent)
        }
        dialogBuilder.setNegativeButton("Delete") { dialog, id ->

            db.collection("facilities").document(text)
                .delete().addOnSuccessListener {
                    Toast.makeText(this, "Deleting entry", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener {
                    Toast.makeText(
                        this, "" +
                                "Failed to delete", Toast.LENGTH_SHORT
                    ).show()
                }
            val intent = Intent(this, FacilitiesActivity::class.java)
            startActivity(intent)
        }

        val alert = dialogBuilder.create()
        alert.show()

    }


}