package com.example.admin_app.vaccine

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_app.R
import com.example.admin_app.databinding.FragmentVaccineCertificatesBinding
import com.example.admin_app.databinding.FragmentVaccinesBinding
import com.example.admin_app.doctor.Doctorslist
import com.example.admin_app.reports.pdfviewer
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

var vaccine_list : ArrayList<vaccine> =  arrayListOf<vaccine>()
var vaccine_id : String = ""
class VaccineCertificatesFragment : Fragment() , VaccineAdapter.Click {
    private lateinit var recyclerView: RecyclerView
    val db = FirebaseFirestore.getInstance()
    private var _binding: FragmentVaccineCertificatesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVaccineCertificatesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addpres.setOnClickListener {
            val intent = Intent(this.context, addvaccine::class.java)
            startActivity(intent)

        }
        recyclerView = binding.RecyclerView
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        vaccine_list=arrayListOf<vaccine>()
        if (!Doctorslist().isConnected(requireContext())) {
            Toast.makeText(requireContext(), "No Internet ", Toast.LENGTH_SHORT).show();
            Log.e("network--->","if-block")
            progressDialog.dismiss()
        }
        db.collection("Vaccine")
            .get()
            .addOnSuccessListener {
                    datas->

                for ( i in datas){
                    Log.e("list","${i}")
                    val obj = vaccine(
                        i.data["vaccine_id"].toString(),
                        i.data["pemial"].toString(),
                        i.data["pname"].toString(),
                        i.data["date"].toString(),
                        i.data["vname"].toString(),
                        i.data["pdf"].toString()
                    )
                    vaccine_list.add(obj)
                }
                recyclerView.adapter = VaccineAdapter(
                    vaccine_list,this,this)
                progressDialog.dismiss()
                Log.e("list", "$vaccine_list")
            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                filter(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    fun filter(text: String) {
        val filtered: ArrayList<vaccine> = ArrayList()

        val courseAry: ArrayList<vaccine> = vaccine_list
        for (eachCourse in courseAry) {
            if (eachCourse.date!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.date!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            }
            else if (eachCourse.pname!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.pname!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            }
            else if (eachCourse.vname!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.vname!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            }
            else if (eachCourse.vaccine_id!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.vaccine_id!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
//
            }

            //calling a method of the adapter class and passing the filtered list
//        DoctorsAdapter(this,filtered)
            recyclerView.adapter  = VaccineAdapter(
                filtered,this,this) }
    }
    override fun go_to_main_view(url: String, r_id: String) {
        pdfviewer().seturl(url)
        vaccine_id=r_id
        val intent = Intent(this.context, pdfviewer::class.java)
        startActivity(intent)

    }

    override fun go_to_main_delete(rid: String) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Do you want delete?")
            .setCancelable(true)
        dialogBuilder.setPositiveButton("Cancel"){
                dialog,id->
        }
        dialogBuilder.setNegativeButton("Delete"){
                dialog,id->
//            val Id= reports[position].id
//            val url = doctor[position].img_url
            val ref = FirebaseStorage.getInstance().getReference("/vaccine/$rid")
            ref.delete().addOnSuccessListener { Log.e("Deleting","---> img Deleted") }
                .addOnFailureListener{ Log.e("Deleting","---> img not deleted")}
            db.collection("Vaccine").document(rid.toString())
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
