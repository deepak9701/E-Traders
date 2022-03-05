package com.android.e_traders

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class SignUpActivity : AppCompatActivity() {
    lateinit var selectedGender: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setSupportActionBar(findViewById(R.id.toolbar4))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val fullName: EditText = findViewById(R.id.editFullName)
        val eMail: EditText = findViewById(R.id.editEMail)
        val mobileNumber: EditText = findViewById(R.id.editMobileNumber)
        val gender: Spinner = findViewById(R.id.genderSpinner)
        val password: EditText = findViewById(R.id.editPassword)
        val confPassword: EditText = findViewById(R.id.editConfPassword)
        val signUpButton: Button = findViewById(R.id.buttonSignUp)
        val fAuth = FirebaseAuth.getInstance()

        val genderAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.gender,
            android.R.layout.simple_spinner_item
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gender.adapter = genderAdapter
        gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedGender = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        signUpButton.setOnClickListener {
            val fName = fullName.text.toString()
            val mNumber = mobileNumber.text.toString()
            val email = eMail.text.toString()
            val passwd = password.text.toString()
            val confPasswd = confPassword.text.toString()
            if (fName.isEmpty()) {
                fullName.error = "Full Name can't be empty!"
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                eMail.error = "E-Mail can't be empty!"
                return@setOnClickListener
            }
            if (mNumber.isEmpty()) {
                mobileNumber.error = "Mobile Number can't be empty!"
                return@setOnClickListener
            }

            if (passwd.isEmpty()) {
                password.error = "Password can't be empty!"
                return@setOnClickListener
            }
            if (confPasswd.isEmpty()) {
                confPassword.error = "Confirm password can't be empty!"
                return@setOnClickListener
            }
            if (confPasswd != passwd) {
                confPassword.error = "Passwords don't match!"
                return@setOnClickListener
            }
            fAuth.createUserWithEmailAndPassword(email, passwd)
                .addOnCompleteListener { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        assert(user != null)
                        val currentUser = user?.uid
                        val databaseReference =
                            FirebaseDatabase.getInstance().reference.child("Users")
                                .child(currentUser.toString())
                        Log.d("Database reference",databaseReference.toString())
                        val usersMap =
                            HashMap<String, Any>()
                        usersMap["Full Name"] = fName
                        usersMap["Mobile Number"] = mNumber
                        usersMap["E-Mail"] = email
                        usersMap["Gender"] = selectedGender
                        databaseReference.setValue(usersMap)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "Users' details successfully added to database!",
                                    Toast.LENGTH_LONG
                                ).show()
                                startActivity(
                                    Intent(
                                        this,
                                        LogInActivity::class.java
                                    )
                                )
                                finish()
                            }.addOnFailureListener { e: Exception ->
                                Toast.makeText(
                                    this,
                                    e.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        Toast.makeText(
                            this,
                            "User registration unsuccessful!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }.addOnFailureListener { e: Exception ->
                    Toast.makeText(
                        this,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}