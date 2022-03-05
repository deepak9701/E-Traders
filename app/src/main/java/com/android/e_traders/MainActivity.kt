package com.android.e_traders

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar1))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val drawer1: DrawerLayout = findViewById(R.id.drawer1)
        val navView: NavigationView = findViewById(R.id.navigationView)
        toggle = ActionBarDrawerToggle(this, drawer1, R.string.open, R.string.close)
        drawer1.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navProfile -> {
                    startActivity(Intent(this, ProfileViewActivity::class.java))
                }
                R.id.navSettings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
                R.id.navShare -> {
                    val shareIntent = Intent()
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_EMAIL, "dpk9701@gmail.com")
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "App Feedback!")
                    startActivity(Intent.createChooser(shareIntent, "Share :"))
                }
                R.id.navRate ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/")))
                R.id.navFeedback -> {
                    val mailIntent = Intent(Intent.ACTION_SENDTO)
                    mailIntent.data = Uri.parse("mailto:dpk9701@gmail.com")
                    mailIntent.putExtra(Intent.EXTRA_SUBJECT, "App Feedback!")
                    startActivity(mailIntent)
                }
                R.id.navCatalog ->
                    startActivity(Intent(this, CatalogActivity::class.java))
                R.id.navOrders ->
                    startActivity(Intent(this, OrdersActivity::class.java))
            }
            drawer1.closeDrawers()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val drawer1 = findViewById<DrawerLayout>(R.id.drawer1)
                drawer1.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}