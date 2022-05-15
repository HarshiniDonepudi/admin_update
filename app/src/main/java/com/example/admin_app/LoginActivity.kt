package com.example.admin_app

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admin_app.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
var admin_main_data = admindata()

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        var db_admin = FirebaseFirestore.getInstance()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        sharedPreferences=this.getSharedPreferences("login",
            AppCompatActivity.MODE_PRIVATE
        )
       val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)
        val checklogin = sharedPreferences.getBoolean("logged", false)
        if (checklogin == true) {
                val intent = Intent(this, DandPActivity::class.java)
                startActivity(intent)
                finish()
        }
        binding.btnlogin.setOnClickListener{
            if(checking()){
                val email=binding.inputEmail.text.toString()
                val password= binding.inputPassword.text.toString()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            load()
                            Toast.makeText(this, "Logged in", Toast.LENGTH_LONG).show()

                            sharedPreferences.edit().putBoolean("logged",true).apply()

                            //-------------------------------
                            var admin_info = admindata()
                            val docRef = db_admin.collection("ADMINS").document(email)
                            docRef.get()
                                .addOnSuccessListener { tasks ->
                                    admin_info = admindata(
                                        tasks.get("id").toString(),
                                        tasks.get("name").toString(),
                                        tasks.get("email").toString())
                                    Log.e("admin","${admin_info}")
                                    sharedPreferences.edit().putString("logged_email",admin_info.email).apply()
                                    sharedPreferences.edit().putString("logged_id",admin_info.id).apply()
                                    sharedPreferences.edit().putString("logged_name",admin_info.name).apply()
                                    finish()
                                    val intent = Intent(this, DandPActivity::class.java)
                                    startActivity(intent)
                                }

                                .addOnFailureListener{
                                    Toast.makeText(this, "Unable to fetch data", Toast.LENGTH_LONG).show()

                                }

                            //-------------------------------

                        } else {
                            Toast.makeText(this, "Wrong Details", Toast.LENGTH_LONG).show()
                        }
                    }
            }
            else{
                Toast.makeText(this,"Enter the Details", Toast.LENGTH_LONG).show()
            }

        }
    }
    private fun load() {

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading....")
        progressDialog.setCancelable(false)
        progressDialog.show()


    }
    fun checking():Boolean
    {
        if(binding.inputEmail.text.toString().trim{it<=' '}.isNotEmpty()
            && binding.inputPassword.text.toString().trim{it<=' '}.isNotEmpty())
        {
            return true
        }
        return false
    }
}