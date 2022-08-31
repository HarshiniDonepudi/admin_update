package com.example.admin_app.vaccine

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_app.*
import com.example.admin_app.databinding.FragmentVaccinesBinding
import com.example.admin_app.doctor.Doctorslist
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VaccinesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
var r_id=""
var url_img=""
class VaccinesFragment : Fragment() ,NewVaccineAdapter.Click {
    private lateinit var recyclerView: RecyclerView
    private lateinit var vaccineAdapter: NewVaccineAdapter
    private var _binding: FragmentVaccinesBinding? = null
    private val binding get() = _binding!!
    var db = Firebase.firestore

    private var vaccineList = ArrayList<newvaccine>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVaccinesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pic.setOnClickListener {

            val intent = Intent(this.context,NewVaccineActivity::class.java)
            startActivity(intent)
        }
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(this.context, 2)



        val progressDialog = ProgressDialog(this.context)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        if (!Doctorslist().isConnected(requireContext())) {
            Toast.makeText(requireContext(), "No Internet ", Toast.LENGTH_SHORT).show();
            Log.e("network--->","if-block")
            progressDialog.dismiss()
        }
        db.collection("Newvaccine").get()
            .addOnSuccessListener { datas ->
                for (i in datas) {
                    Log.e("list", "${i}")
                    val obj = newvaccine(
                        i.data["image"].toString(),
                        i.data["text"].toString(),
                        i.data["distributor"].toString(),
                        i.data["dose"].toString(),
                        i.data["symptoms"].toString(),

                    )

                    vaccineList.add(obj)
                }
                vaccineAdapter = NewVaccineAdapter(vaccineList, this@VaccinesFragment)
                recyclerView.adapter = vaccineAdapter
                progressDialog.dismiss()


            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }

    }

    override fun go_to_main_view(text: String, url: String) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Do you want to delete this picture?")
            .setCancelable(true)
        dialogBuilder.setPositiveButton("Edit"){
                dialog,id->
            r_id=text
            url_img =url

            val intent = Intent(this.context, EditVaccineActivity::class.java)
            startActivity(intent)
        }
        dialogBuilder.setNegativeButton("Delete"){
                dialog,id->

            val ref = FirebaseStorage.getInstance().getReference("/Newvaccine/$text")
            ref.delete().addOnSuccessListener { Log.e("Deleting","---> img Deleted") }
                .addOnFailureListener{Log.e("Deleting","---> img not deleted")}
            db.collection("Newvaccine").document(text.toString())
                .delete().addOnSuccessListener{
                    Toast.makeText(this.context,"Deleting entry", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener{
                    Toast.makeText(this.context,"" +
                            "Failed to delete", Toast.LENGTH_SHORT).show()
                }
            val intent = Intent(this.context, VaccineActivity::class.java)
            startActivity(intent)
        }

        val alert = dialogBuilder.create()
        alert.show()

    }
    }







