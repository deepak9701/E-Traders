package com.android.e_traders

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class EditActivity : AppCompatActivity() {
    lateinit var selectedGender: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val fullName = findViewById<EditText>(R.id.editFullName)
        val eMail = findViewById<EditText>(R.id.editEMail)
        val mobileNumber = findViewById<EditText>(R.id.editMobileNumber)
        val genderSpinner = findViewById<Spinner>(R.id.genderSpinner)
        val buttonUpdate = findViewById<Button>(R.id.buttonSave)
        val buttonClose = findViewById<Button>(R.id.buttonClose)
        val genderAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.gender,
            R.layout.support_simple_spinner_dropdown_item
        )
        genderAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        genderSpinner.adapter = genderAdapter
        genderSpinner.prompt = "Gender"
        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        val user = FirebaseAuth.getInstance().currentUser
        assert(user != null)
        val currentUser = user?.uid
        val databaseReference =
            FirebaseDatabase.getInstance().reference.child("Users").child(currentUser.toString())
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataFullName = snapshot.child("Full Name").getValue(
                    String::class.java
                )
                val dataMobileNumber = snapshot.child("Mobile Number").getValue(
                    String::class.java
                )
                val dataEMail = snapshot.child("E-Mail").getValue(
                    String::class.java
                )
                fullName.setText(dataFullName)
                mobileNumber.setText(dataMobileNumber)
                eMail.setText(dataEMail)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditActivity, error.message, Toast.LENGTH_LONG).show()
            }
        })
        buttonUpdate.setOnClickListener{
            val fName: String = fullName.text.toString()
            val mNumber: String = mobileNumber.text.toString()
            val email: String = eMail.text.toString()
            val gender: String = selectedGender
            updateData(fName, mNumber, email, gender)
        }
        buttonClose.setOnClickListener { finish() }
    }

    private fun updateData(
        fName: String,
        mNumber: String,
        email: String,
        gender: String
    ) {
        val usersMap = HashMap<String, Any>()
        usersMap["Full Name"] = fName
        usersMap["Mobile Number"] = mNumber
        usersMap["E-Mail"] = email
        usersMap["Gender"] = gender
        val user = FirebaseAuth.getInstance().currentUser
        assert(user != null)
        val currentUser = user?.uid
        val databaseReference =
            FirebaseDatabase.getInstance().reference.child("Users").child(currentUser!!)
        databaseReference.updateChildren(usersMap)
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@EditActivity,
                        "User details updated successfully!",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@EditActivity,
                        "User details could not be updated",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }.addOnFailureListener { e: Exception ->
                Toast.makeText(
                    this@EditActivity,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
    }
}