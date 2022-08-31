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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_app.*
import com.example.admin_app.databinding.FragmentVaccineRequestBinding
import com.example.admin_app.doctor.Doctorslist
import com.example.admin_app.doctor.position
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VaccineRequestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
    var status =""
var vac=""
    lateinit  var requestlist : ArrayList<bookvaccine>
    class VaccineRequestFragment : Fragment(),VaccineRequestsAdapter.Click{
        private lateinit var recyclerView: RecyclerView
        private lateinit var db: FirebaseFirestore
        private var _binding: FragmentVaccineRequestBinding? = null
        private val binding get() = _binding!!
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            db = FirebaseFirestore.getInstance()
            _binding = FragmentVaccineRequestBinding.inflate(inflater, container, false)
            val view = binding.root
            return view
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            recyclerView = binding.RecyclerView
            (activity as AppCompatActivity).supportActionBar?.title="Appointments"
            super.onViewCreated(view, savedInstanceState)
            val progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Fetching data....")
            progressDialog.setCancelable(false)
            progressDialog.show()
            requestlist = arrayListOf<bookvaccine>()
            if (!Doctorslist().isConnected(requireContext())) {
                Toast.makeText(requireContext(), "No Internet ", Toast.LENGTH_SHORT).show();
                Log.e("network--->","if-block")
                progressDialog.dismiss()
            }
            db.collection("VaccineRequests")
                .get()
                .addOnSuccessListener {
                        datas->

                    for ( i in datas){
                        Log.e("list","${i}")
                        val obj = bookvaccine(
                            i.data["vaccine_id"].toString(),
                            i.data["name"].toString(),
                            i.data["distributor"].toString(),
                            i.data["patient_emial"].toString(),
                            i.data["dose"].toString(),
                            i.data["other"].toString(),
                            i.data["status"].toString()
                        )
                        requestlist.add(obj)
                    }
                    recyclerView.adapter = VaccineRequestsAdapter(this@VaccineRequestFragment, requestlist,this@VaccineRequestFragment)
                    progressDialog.dismiss()

                }
                .addOnFailureListener { exception ->
                    Log.e("---->","Error getting documents: ")
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
            val filtered: ArrayList<bookvaccine> = ArrayList()

            val courseAry: ArrayList<bookvaccine> = requestlist

            for (eachCourse in courseAry) {
                if (eachCourse.dose!!.toLowerCase()
                        .contains(text.toLowerCase()) || eachCourse.distributor!!.toLowerCase()
                        .contains(text.toLowerCase())
                ) {
                    filtered.add(eachCourse)
                }
                else if( eachCourse.name!!.toLowerCase()
                        .contains(text.toLowerCase()) || eachCourse.distributor!!.toLowerCase()
                        .contains(text.toLowerCase())){
                    filtered.add(eachCourse)
                }
                else if( eachCourse.status!!.toLowerCase()
                        .contains(text.toLowerCase()) || eachCourse.distributor!!.toLowerCase()
                        .contains(text.toLowerCase())){
                    filtered.add(eachCourse)
                }
                else if( eachCourse.other!!.toLowerCase()
                        .contains(text.toLowerCase()) || eachCourse.distributor!!.toLowerCase()
                        .contains(text.toLowerCase())){
                    filtered.add(eachCourse)
                }
                else if( eachCourse.vaccine_id!!.toLowerCase()
                        .contains(text.toLowerCase()) || eachCourse.distributor!!.toLowerCase()
                        .contains(text.toLowerCase())){
                    filtered.add(eachCourse)
                }
            }

            //calling a method of the adapter class and passing the filtered list
//        DoctorsAdapter(this,filtered)
            recyclerView.adapter = VaccineRequestsAdapter(this@VaccineRequestFragment, filtered,this@VaccineRequestFragment)
        }

        override fun go_to_main_view(text: String, vacid: String) {
           status = text
            vac = vacid
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage("Do you want to Confirm the time of Vaccine Appointment?")
                .setCancelable(true)
            dialogBuilder.setPositiveButton("Confirm"){
                    dialog,id->
                pos = id
                val intent = Intent(this.context, ConfirmVaccineActivity::class.java)
                startActivity(intent)
            }
            dialogBuilder.setNegativeButton("Cancel"){
                    dialog,id->

            }

            val alert = dialogBuilder.create()
            alert.show()

        }


    }