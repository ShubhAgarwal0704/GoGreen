package com.example.gogreen.User_Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.gogreen.Login_Register.LoginScreen
import com.example.gogreen.databinding.ActivityUsersActivityBinding
import com.example.gogreen.userAdapter.HomePagerAdapter
import com.example.gogreen.userFragments.CartFragment
import com.example.gogreen.userFragments.HomeFragment
import com.example.gogreen.userFragments.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import nl.joery.animatedbottombar.AnimatedBottomBar


class UsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsersActivityBinding
    var fragmentsList: ArrayList<Fragment>? = null
    var viewPagerAdapter: HomePagerAdapter? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            Toast.makeText(this@UsersActivity, "User Logged out!!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
            finish()
        }

        fragmentsList = ArrayList()
        fragmentsList!!.add(HomeFragment())
        fragmentsList!!.add(CartFragment())
        fragmentsList!!.add(ProfileFragment())

        viewPagerAdapter = HomePagerAdapter(supportFragmentManager, lifecycle, fragmentsList)
        binding.viewPager.offscreenPageLimit = fragmentsList!!.size
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = viewPagerAdapter

        binding.bottomBar.selectTabAt(0, false)
        binding.bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                curTab: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                binding.viewPager.currentItem = curTab
            }
        })

    }

    override fun onResume() {
        super.onResume()
        val preferences = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = preferences.edit()
        if (preferences.getBoolean("isCart",false)){
            binding.bottomBar.selectTabAt(1,true)
            editor.putBoolean("isCart", false)
            editor.apply()
        }
    }
}

