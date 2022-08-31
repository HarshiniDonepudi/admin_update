package com.example.admin_app

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_app.databinding.ActivityDiagnosisBinding
import com.example.admin_app.databinding.ActivityOurTeamBinding
import com.example.admin_app.doctor.Doctorslist
import com.example.admin_app.doctor.position
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

lateinit  var teamlist : ArrayList<team>
class OurTeamActivity : AppCompatActivity(), TeamAdapter.Click {
    var db = Firebase.firestore
    private lateinit var recyclerView: RecyclerView

    private lateinit var binding: ActivityOurTeamBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOurTeamBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)
        recyclerView = binding.teamList
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        Log.e("dfdf", "dfdsfs")
        binding.add.setOnClickListener {

            val intent = Intent(this, AddTeamActivity::class.java)
            startActivity(intent)
        }



        retrive()
    }

    private fun retrive() {
        teamlist = arrayListOf<team>()
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        if (!Doctorslist().isConnected(applicationContext)) {
            Toast.makeText(applicationContext, "No Internet ", Toast.LENGTH_SHORT).show();
            Log.e("network--->","if-block")
            progressDialog.dismiss()
        }
        db.collection("Team").get()
            .addOnSuccessListener { datas ->
                for (i in datas) {
                    Log.e("list", "${i}")
                    val obj = team(
                        i.data["name"].toString(),
                        i.data["designation"].toString(),
                        i.data["number"].toString(),
                        i.data["about"].toString(),
                        i.data["image"].toString()

                    )

                    teamlist.add(obj)
                }
                recyclerView.adapter = TeamAdapter(teamlist, this@OurTeamActivity,this@OurTeamActivity)
                progressDialog.dismiss()

            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }

    }

    override fun go_to_main_delete(R_id: Int) {
        val name = teamlist[position].name
        val ref = FirebaseStorage.getInstance().getReference("/img/$name")
        ref.delete().addOnSuccessListener { Log.e("Deleting","---> img Deleted") }
            .addOnFailureListener{Log.e("Deleting","---> img not deleted")}
        db.collection("Team").document(name)
            .delete().addOnSuccessListener{
                Toast.makeText(this,"Deleting entry", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener{
                Toast.makeText(this,"" +
                        "Failed to delete", Toast.LENGTH_SHORT).show()
            }
        val intent = Intent(this, OurTeamActivity::class.java)
        startActivity(intent)
    }





    override fun go_to_main_edit(R_id: Int) {

        position = R_id
        val intent = Intent(this, EditTeamActivity::class.java)
        startActivity(intent)
    }
    override fun onBackPressed() {
        val intent= Intent(this,OthersActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }


}