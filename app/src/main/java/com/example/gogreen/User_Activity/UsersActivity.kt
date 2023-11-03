
package com.example.gogreen.User_Activity




import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.gogreen.R
import com.example.gogreen.databinding.ActivityUsersActivityBinding
import com.example.gogreen.userFragments.CartFragment
import com.example.gogreen.userFragments.HomeFragment
import com.example.gogreen.userFragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class UsersActivity : AppCompatActivity() {

    private lateinit var binding:ActivityUsersActivityBinding
    private val homeFragment = HomeFragment()
    private val cartFragment = CartFragment()
    private val profileFragment = ProfileFragment()
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Set the HomeFragment as the initial fragment
        setCurrentFragment(homeFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
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

