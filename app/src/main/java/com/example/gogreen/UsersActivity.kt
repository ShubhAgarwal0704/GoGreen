package com.example.gogreen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.gogreen.Login_Register.LoginScreen
import com.example.gogreen.databinding.ActivityUsersActivityBinding
import com.example.gogreen.userFragments.CartFragment
import com.example.gogreen.userFragments.HomeFragment
import com.example.gogreen.userFragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class UsersActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUsersActivityBinding
    private val homeFragment = HomeFragment()
    private val cartFragment = CartFragment()
    private val profileFragment = ProfileFragment()
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState:Bundle?) {


            super.onCreate(savedInstanceState)
            this.binding = ActivityUsersActivityBinding.inflate(layoutInflater)

            setContentView(binding.root)
        auth =  FirebaseAuth.getInstance()
        val logoutbutton = findViewById<Button>(R.id.logoutbutton)
        logoutbutton.setOnClickListener {
            auth.signOut()
            Toast.makeText(this@UsersActivity, "User Logged out!!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
            finish()
        }

            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)


            // Set the HomeFragment as the initial fragment
            setCurrentFragment(homeFragment)

            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home->setCurrentFragment(homeFragment)
                    R.id.nav_cart->setCurrentFragment(cartFragment)
                    R.id.nav_profile->setCurrentFragment(profileFragment)
                }
                true
            }
        }

        private fun setCurrentFragment(fragment:Fragment) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, fragment)
                commit()
            }
        }
    }

