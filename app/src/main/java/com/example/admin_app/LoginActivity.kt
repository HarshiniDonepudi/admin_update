package com.example.admin_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase


class LoginActivity : AppCompatActivity() {

//    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_login)
        super.onCreate(savedInstanceState)
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        val view = binding.root

        val login : Button=findViewById(R.id.btnlogin)
        login.setOnClickListener{
            val intent = Intent(this, DandPActivity::class.java)
            startActivity(intent)
        }
    }
}