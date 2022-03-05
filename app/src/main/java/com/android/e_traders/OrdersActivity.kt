package com.android.e_traders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class OrdersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        findViewById<Button>(R.id.buttonClose).setOnClickListener { finish() }
    }
}