package com.example.gogreen.Login_Register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gogreen.R
import com.example.gogreen.UsersActivity


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

@Suppress("NAME_SHADOWING")
class RegisterScreen : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var gender: String
    private lateinit var passwordEditText: EditText
    private lateinit var confirmEditText: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var usersRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen)
        nameEditText = findViewById(R.id.fullNameEditText)
        emailEditText = findViewById(R.id.registerEmailEditText)
        addressEditText = findViewById(R.id.addressEditText)
        usernameEditText = findViewById(R.id.registerUsernameEditText)
        passwordEditText = findViewById(R.id.registerPasswordEditText)
        confirmEditText = findViewById(R.id.confirmPasswordEditText)
        val submitButton: Button = findViewById(R.id.registerButton)
        val spinner = findViewById<Spinner>(R.id.spinnerGender)

        val genderOptions = arrayOf("Select the gender","Male", "Female", "Others")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, genderOptions)
        adapter.setDropDownViewResource(R.layout.dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0)
        var isGenderSelected = false
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

        submitButton.setOnClickListener {
//            val intent = Intent(this@RegisterScreen,LoginScreen::class.java)
//            startActivity(intent)
//            this.finish()

            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val address = addressEditText.text.toString()
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmEditText.text.toString()
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
                        Toast.makeText(
                            this@RegisterScreen,
                            errorMessage ?: "Unknown error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        }

    }
}





