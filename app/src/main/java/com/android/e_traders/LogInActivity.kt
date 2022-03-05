package com.android.e_traders

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        val buttonLogIn = findViewById<Button>(R.id.buttonLogIn)
        val userEMailAddress = findViewById<EditText>(R.id.userEMailAddress)
        val userPassword = findViewById<EditText>(R.id.userPassword)
        findViewById<TextView>(R.id.registerTextView).setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        buttonLogIn.setOnClickListener {
            if (userEMailAddress.text.toString().isEmpty()) {
                userEMailAddress.error = "Please enter your registered E-Mail Address!"
                return@setOnClickListener
            }
            if (userPassword.text.toString().isEmpty()) {
                userPassword.error = "Please enter your password!"
                return@setOnClickListener
            }
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                userEMailAddress.text.toString(),
                userPassword.text.toString()
            ).addOnSuccessListener {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }.addOnFailureListener { e: Exception ->
                Toast.makeText(
                    this,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    override fun onStart() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        super.onStart()
    }
}