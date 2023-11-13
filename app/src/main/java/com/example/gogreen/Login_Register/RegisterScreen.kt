package com.example.gogreen.Login_Register

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gogreen.R
import com.example.gogreen.databinding.ActivityRegisterScreenBinding


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterScreen : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterScreenBinding
    private lateinit var gender: String
    private lateinit var auth: FirebaseAuth
    private lateinit var usersRef: DatabaseReference
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = ProgressDialog(this)
        dialog!!.setTitle("Please Wait...")
        dialog!!.isIndeterminate = true

        val genderOptions = arrayOf("Select the gender","Male", "Female", "Others")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, genderOptions)
        
        adapter.setDropDownViewResource(R.layout.dropdown_item)
        binding.spinnerGender.adapter = adapter
        binding.spinnerGender.setSelection(0)
        var isGenderSelected = false
        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Check if a valid selection (not the hint) is made
                if (position != 0) {
                    isGenderSelected = true
                    gender = genderOptions[position] // Capture the selected gender
                } else {
                    isGenderSelected = false
                    gender = "" // Reset gender when nothing is selected
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                Toast.makeText(this@RegisterScreen, "Please select a gender", Toast.LENGTH_SHORT).show()
            }
        }
        auth = FirebaseAuth.getInstance()

        val database = FirebaseDatabase.getInstance()
        usersRef = database.getReference("users")

        binding.alreadyHasAccountTextView.setOnClickListener {
            onBackPressed()
        }

        binding.registerButton.setOnClickListener {
            val name = binding.fullNameEditText.text.toString()
            val email = binding.registerEmailEditText.text.toString()
            val address = binding.addressEditText.text.toString()
            val username = binding.registerUsernameEditText.text.toString()
            val password = binding.registerPasswordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this@RegisterScreen, "Invalid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!isGenderSelected) {
                Toast.makeText(this@RegisterScreen, "Please select a gender", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                Toast.makeText(this@RegisterScreen, "Password and confirm password do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isFinishing) {
                dialog?.show()
            }
            Log.d("Firebase", "About to write data to Firebase")

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val userId = user?.uid
                        if (userId != null) {
                            val userData = User(name, email, address, username, gender, password, false)

                            // Set user data in the Realtime Database
                            usersRef.child(userId).setValue(userData)
                                .addOnCompleteListener { databaseTask ->
                                    if (!isFinishing) {
                                        dialog?.dismiss()
                                    }
                                    if (databaseTask.isSuccessful) {
                                        // Data was written successfully
                                        Log.d("Database", "Data written successfully")
                                        val intent = Intent(this@RegisterScreen,LoginScreen::class.java)
                                        startActivity(intent)
                                        this.finish()
                                    } else {
                                        val errorMessage = databaseTask.exception?.message
                                        Log.e("DatabaseError", errorMessage ?: "Unknown error")
                                        Toast.makeText(
                                            this@RegisterScreen,
                                            errorMessage ?: "Unknown error",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    } else {
                        val errorMessage = task.exception?.message
                        Log.e("FirebaseError", errorMessage ?: "Unknown error")
                        if (!isFinishing) {
                            dialog?.dismiss()
                        }
                        Toast.makeText(
                            this@RegisterScreen,
                            errorMessage ?: "Unknown error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}





