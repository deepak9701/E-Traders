package com.android.e_traders

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)
        setSupportActionBar(findViewById(R.id.toolbar3))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val buttonLogOut = findViewById<Button>(R.id.buttonLogOut)
        val textFullName = findViewById<TextView>(R.id.textFullName2)
        val textEMail = findViewById<TextView>(R.id.textEMail2)
        val textMobileNumber = findViewById<TextView>(R.id.textMobileNumber2)
        val textGender = findViewById<TextView>(R.id.textGender2)

        val user = FirebaseAuth.getInstance().currentUser
        val currentUser = user?.uid
        val databaseReference =
            FirebaseDatabase.getInstance().reference.child("Users").child(currentUser.toString())
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataFullName = snapshot.child("Full Name").getValue(String::class.java)
                val dataMobileNumber = snapshot.child("Mobile Number").getValue(String::class.java)
                val dataEMail = snapshot.child("E-Mail").getValue(String::class.java)
                val dataGender = snapshot.child("Gender").getValue(String::class.java)
                textFullName.text = dataFullName
                textMobileNumber.text = dataMobileNumber
                textEMail.text = dataEMail
                textGender.text = dataGender
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileViewActivity, error.message, Toast.LENGTH_LONG).show()
            }
        })

        findViewById<Button>(R.id.buttonEdit).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    EditActivity::class.java
                )
            )
        }

        if (FirebaseAuth.getInstance().currentUser == null) {
            Toast.makeText(this, "No Users Found. Please logIn to continue.", Toast.LENGTH_LONG)
                .show()
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }

        buttonLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
