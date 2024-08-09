package com.example.febafriends

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.example.febafriends.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    lateinit var navigationView: NavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar: Toolbar
    lateinit var toolbarTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawermain)
        navigationView = findViewById(R.id.navigationview)
        toolbar = findViewById(R.id.maintoolbar)
        toolbarTextView = findViewById(R.id.toolbarActivityName)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbarTextView.text = "Home"

        val actionBarToggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer
        )
        actionBarToggle.getDrawerArrowDrawable().setColor(resources.getColor(R.color.red))
        drawerLayout.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()


        loadFragment(HomeFragment())
        setUpNavigationView()
    }

    private fun setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.home -> {
                    toolbarTextView.text = "Home"
                    loadFragment(HomeFragment())
                }
                R.id.aboutus -> {
                    toolbarTextView.text = "About Us"
                    loadFragment(AboutUsFragment())
                }
                R.id.contactus -> {
                    toolbarTextView.text = "Contact Us"
                    loadFragment(ContactusFragment())
                }
                R.id.privacypolicy -> {
                    toolbarTextView.text = "Privacy Policy"
                    loadFragment(PrivacyPolicyFragment())
                }
                R.id.login -> {
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                }
                R.id.logout -> {
                    logOut()
                    true
                }
            }
            drawerLayout.closeDrawers()
            true
        }


        navigationView.itemBackground = resources.getDrawable(R.drawable.menu_background, null)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView)
        } else {
            super.onBackPressed()
        }
    }
    fun loadFragment(fragment: Fragment) {

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.content_frame, fragment)
            transaction.commit()
    }
    fun logOut(){
        Exoplayer.getInstance()?.release()
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, Login::class.java))
        finish()
    }
    override fun onResume() {
        super.onResume()

    }

}
