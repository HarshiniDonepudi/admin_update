package com.example.admin_app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.admin_app.databinding.ActivityLoginBinding
import com.example.admin_app.doctor.Doctorslist
import com.example.admin_app.doctor.MainActivity
import com.google.firebase.auth.FirebaseAuth

class DandPActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dand_pactivity)
        val doctors: Button = findViewById(R.id.doctors)
        val patients: Button = findViewById(R.id.patients)
        val others: Button = findViewById(R.id.others)
        doctors.setOnClickListener {
            val intent = Intent(this, Doctorslist::class.java)
            startActivity(intent)
        }
        patients.setOnClickListener {
            val intent = Intent(this, PatientProfileActivity::class.java)
            startActivity(intent)
        }
        others.setOnClickListener {
            val intent = Intent(this, OthersActivity::class.java)
            startActivity(intent)
        }




    }



        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.custom_menu, menu)

            sharedPreferences=this.getSharedPreferences("login",
                AppCompatActivity.MODE_PRIVATE
            )

            val admin_info = admindata()
            val loggedemail = sharedPreferences
                .getString("logged_email", admin_info.email)
            Log.e("logged","${loggedemail}")

            if(loggedemail=="lionscancerhospital@gmail.com")
                menu?.findItem(R.id.adduser)?.setEnabled(true)
            else
                menu?.findItem(R.id.adduser)?.setVisible(false)
            return super.onCreateOptionsMenu(menu)


        }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.adduser -> {
                val intent = Intent(this, NewUserActivity::class.java)
                startActivity(intent)
                return true

            }
            R.id.logout->{

                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage("Do you want to logout?")
                    .setCancelable(true)
                dialogBuilder.setPositiveButton("Cancel"){
                        dialog,id->
                }
                dialogBuilder.setNegativeButton("logout"){
                        dialog,id->
                    sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
                    sharedPreferences.edit().putBoolean("logged",false).apply()
                    Toast.makeText(
                        applicationContext,
                        "logging out from account",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }

                val alert = dialogBuilder.create()
                alert.show()
                return true
            }
            else -> {super.onOptionsItemSelected(item)}
        }
    }
}