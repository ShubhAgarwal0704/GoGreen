package com.example.gogreen.Login_Register

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.gogreen.AdminActivity
import com.example.gogreen.User_Activity.UsersActivity
import com.example.gogreen.databinding.ActivityLoginScreenBinding


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginScreen : AppCompatActivity() {

    private lateinit var binding: ActivityLoginScreenBinding
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = ProgressDialog(this)
        dialog!!.setTitle("Please Wait...")
        dialog!!.isIndeterminate = true

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                isValidLogin(email, password)
            } else {
                showToast("Please Enter Email and Password.")
            }
        }

        binding.createAccountTextView.setOnClickListener {
            val intent = Intent(this, RegisterScreen::class.java)
            startActivity(intent)
        }

        binding.forgotPasswordTextView.setOnClickListener {
            showToast("Coming Soon!")
        }
    }

    private fun isValidLogin(email: String, password: String) {
        if (!isFinishing) {
            dialog?.show()
        }
        val auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    if (user != null) {
                        val userId = user.uid
                        val databaseReference =
                            FirebaseDatabase.getInstance().getReference("users/$userId")

                        databaseReference.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    if (!isFinishing) {
                                        dialog?.dismiss()
                                    }
                                    val isAdmin = dataSnapshot.child("status")
                                        .getValue(Boolean::class.java)

                                    if (isAdmin == true) {
                                        // User is an admin, go to AdminActivity
                                        val intent = Intent(
                                            this@LoginScreen,
                                            AdminActivity::class.java
                                        )
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        // User is not an admin, go to UsersActivity
                                        val intent = Intent(
                                            this@LoginScreen,
                                            UsersActivity::class.java
                                        )
                                        startActivity(intent)
                                        finish()
                                    }
                                } else {
                                    Log.e("FirebaseDatabase", "User data does not exist")
                                    if (!isFinishing) {
                                        dialog?.dismiss()
                                    }
                                    showToast("User data not found")
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Log.e(
                                    "FirebaseDatabase",
                                    "Error fetching user data: ${databaseError.message}"
                                )
                                if (!isFinishing) {
                                    dialog?.dismiss()
                                }
                                showToast("Error: ${databaseError.message}")
                            }
                        })
                    }
                } else {
                    if (!isFinishing) {
                        dialog?.dismiss()
                    }
                    showToast("Login Failed. Please check your credentials.")
                }
            }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
