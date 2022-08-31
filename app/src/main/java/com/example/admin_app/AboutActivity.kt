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
import com.example.admin_app.databinding.ActivityAboutBinding
import com.example.admin_app.doctor.Doctorslist
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

var pos: Int =-1
var aboutid = ""
lateinit  var aboutList : ArrayList<about>
class AboutActivity : AppCompatActivity(),AboutAdapter.Click {
    var db = Firebase.firestore
    private lateinit var recyclerView: RecyclerView

    private lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)



           binding = ActivityAboutBinding.inflate(layoutInflater)
            val view = binding.root

            setContentView(view)
            recyclerView = binding.recyclerView
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(true)
            Log.e("dfdf", "dfdsfs")
            binding.pic.setOnClickListener{
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
                val intent = Intent(this, AddAboutActivity::class.java)
                startActivity(intent)
            }



            aboutList = arrayListOf<about>()
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Fetching data....")
            progressDialog.setCancelable(false)
            progressDialog.show()
        if (!Doctorslist().isConnected(applicationContext)) {
            Toast.makeText(applicationContext, "No Internet ", Toast.LENGTH_SHORT).show();
            Log.e("network--->","if-block")
            progressDialog.dismiss()
        }
            db.collection("about").orderBy("date",Query.Direction.ASCENDING).get()
                .addOnSuccessListener { datas ->
                    for (i in datas) {
                        Log.e("list", "${i}")
                        val obj = about(
                            i.data["about_id"].toString(),
                            i.data["text"].toString(),
                            i.data["date"].toString(),
                        )

                        aboutList.add(obj)

                    }
                    recyclerView.adapter = AboutAdapter(aboutList,this@AboutActivity)
                    progressDialog.dismiss()
                }
                .addOnFailureListener { exception ->
                    Log.e("---->", "Error getting documents: ")
                }

        }


    override fun go_to_main_view(text: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Do you want to Edit or Delete?")
            .setCancelable(true)
        dialogBuilder.setPositiveButton("Edit"){
                dialog,id->
            aboutid = text
            pos = id
            val intent = Intent(this, EditAboutActivity::class.java)
            startActivity(intent)
        }
        dialogBuilder.setNegativeButton("Delete"){
                dialog,id->

            db.collection("about").document(text)
                .delete().addOnSuccessListener{
                    Toast.makeText(this,"Deleting entry", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener{
                    Toast.makeText(this,"" +
                            "Failed to delete", Toast.LENGTH_SHORT).show()
                }
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        val alert = dialogBuilder.create()
        alert.show()

    }
    override fun onBackPressed() {
        val intent= Intent(this,OthersActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }
    }






