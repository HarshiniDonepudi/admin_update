package com.example.admin_app

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.admin_app.databinding.ActivityLoginBinding
import com.example.admin_app.databinding.ActivityNewUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NewUserActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityNewUserBinding
    private lateinit var db_admin: FirebaseFirestore
    var email = ""
    var password = ""
    var name = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        lateinit var sharedPreferences: SharedPreferences
        db_admin = FirebaseFirestore.getInstance()
        binding = ActivityNewUserBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)
        binding.register.setOnClickListener {
            if (checking()) {
                var email = binding.inputEmail.text.toString()
                var password = binding.inputPassword.text.toString()
                var name = binding.inputUsername.text.toString()
                val data = admindata(
                    "0",
                    name,
                    email,

                )
                val Users = db_admin.collection("ADMINS")
                val query = Users.whereEqualTo("email", email).get()
                    .addOnSuccessListener { tasks ->
                        if (tasks.isEmpty) {
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Users.document(email).set(data)
                                        val intent = Intent(this, DandPActivity::class.java)
                                        startActivity(intent)

                                    Toast.makeText(this,"Registartion Successful", Toast.LENGTH_LONG).show()


                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Authentication Failed",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                        } else {
                            Toast.makeText(
                                this,
                                "User Already Registered",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    }
            } else {
                Toast.makeText(this, "Enter the Details", Toast.LENGTH_LONG).show()
            }
        }
    }



 private fun checking():Boolean{
    if(binding.inputUsername.text.toString().trim{it<=' '}.isNotEmpty()
        && binding.inputEmail.text.toString().trim{it<=' '}.isNotEmpty()
        && binding.inputPassword.text.toString().trim{it<=' '}.isNotEmpty()
    )
    {
        return true
    }
    return false
}

}



