package com.example.admin_app.vaccine

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.admin_app.databinding.ActivityAddvaccineBinding
import com.example.admin_app.databinding.ActivityVaccineBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class addvaccine : AppCompatActivity() {
    var pdfuri : Uri?=null
    var genurl : String =""
    val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityAddvaccineBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddvaccineBinding.inflate(layoutInflater)
        Log.e("add vaccine","Open")
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        binding.date.setText(currentDate)
       binding.rId.setText(""+currentDate.subSequence(0,2)+currentDate.subSequence(3,5)+currentDate.subSequence(6,10)+currentDate.subSequence(11,13)+currentDate.subSequence(14,16)+currentDate.subSequence(17,18).toString())

        binding.selectpdf.setOnClickListener {
            selectpdf()

        }
        binding.button.setOnClickListener {
            binding.file.setText("No file is selected")
            binding.file.setTextColor(Color.parseColor("#FF000000"))
            var pemail = binding.pEmail.text.toString()
            var pname = binding.pName.text.toString()
            var vname = binding.vname.text.toString()
            var flag=0
            if(pemail.isEmpty()){
                flag=2
                binding.pEmail.error="Please enter name!"
                binding.pEmail.requestFocus()
            }
            if(pname.isEmpty()){
                flag=2
                binding.pName.error="Please enter id!"
                binding.pName.requestFocus()
            }
            if(vname.isEmpty()){
                flag=2
                binding.vname.error="Please enter Spec!"
                binding.vname.requestFocus()
            }

            if(pdfuri==null){
                flag=2

                binding.file.setText("SELECT FILE!!!")
                binding.file.setTextColor(Color.parseColor("#FFFF0000"))
            }
            if (flag!=2){
                upload(pdfuri,pemail,pname,vname)
                pdfuri=null
            }

        }


    }

    private fun upload(pdfuri: Uri?, pemail: String, pname: String, vname: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.setMessage("Uploading....")
        progressDialog.setProgress(0)
        progressDialog.show()
        val filename = binding.rId.text.toString()
            val ref = FirebaseStorage.getInstance().getReference("/vaccine/${filename}")
        ref.putFile(pdfuri!!).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                genurl = it.toString()
                val data = vaccine(
                    filename,
                    pemail,
                    pname,
                    binding.date.text.toString(),
                    vname,
                    genurl
                )
                db.collection("Vaccine").document(filename).set(data)

                Toast.makeText(this,"Vaccine uploaded", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, VaccineActivity::class.java)
                startActivity(intent)
            }
                .addOnFailureListener {

                    Log.e("pdfurl:::", "FAILED")
                }
        }.addOnFailureListener{
            Log.e("pdfurl:::", "FAILED")

        }.addOnProgressListener {
            val p : Long = (100 * it.bytesTransferred / it.totalByteCount)
            progressDialog.setProgress(p.toInt())
        }
            .addOnCompleteListener { progressDialog.dismiss() }

    }
    private fun selectpdf() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type="application/pdf"
        startActivityForResult(intent,86)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==86 && resultCode== RESULT_OK && data!=null){
            pdfuri= data.data
            binding.file.setText(data.data!!.lastPathSegment)
            Log.e("pdfurl:::","${data.data!!.path}")
        }
        else{
            Toast.makeText(this,"Pleaes select a file", Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}