package com.example.gogreen.Login_Register

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.gogreen.AdminActivity
import com.example.gogreen.R
import com.example.gogreen.User_Activity.UsersActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this,R.color.colorAccent)
        window.navigationBarColor = ContextCompat.getColor(this,R.color.colorAccent)

        setContentView(R.layout.activity_splash_screen)

        // Delay for 2 seconds and then start the main activity
        Handler(Looper.getMainLooper()).postDelayed({
            val auth = FirebaseAuth.getInstance()
            if (auth.currentUser != null) {
                // User is already authenticated, so go to UserActivity
                val user = auth.currentUser

                if (user != null) {
                    val userId = user.uid
                    val databaseReference =
                        FirebaseDatabase.getInstance().getReference("users/$userId")

                    databaseReference.addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val isAdmin = dataSnapshot.child("status")
                                    .getValue(Boolean::class.java)

                                if (isAdmin == true) {
                                    // User is an admin, go to AdminActivity
                                    val intent = Intent(
                                        this@SplashScreen,
                                        AdminActivity::class.java
                                    )
                                    startActivity(intent)
                                    finish()
                                } else {
                                    // User is not an admin, go to UsersActivity
                                    val intent = Intent(
                                        this@SplashScreen,
                                        UsersActivity::class.java
                                    )
                                    startActivity(intent)
                                    finish()
                                }
                            } else {
                                Log.e("FirebaseDatabase", "User data does not exist")
                                startLogin()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.e(
                                "FirebaseDatabase",
                                "Error fetching user data: ${databaseError.message}"
                            )
                            startLogin()
                        }
                    })
                }
            } else {
                startLogin()
            }
        }, 1000) // 2000 milliseconds
    }

    private fun startLogin(){
        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
