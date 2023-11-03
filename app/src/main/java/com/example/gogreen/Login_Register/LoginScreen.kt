        package com.example.gogreen.Login_Register

        import android.content.Intent
        import androidx.appcompat.app.AppCompatActivity
        import android.os.Bundle
        import android.util.Log
        import android.widget.*
        import com.example.gogreen.AdminActivity
        import com.example.gogreen.R
        import com.example.gogreen.UsersActivity



        import com.google.firebase.auth.FirebaseAuth
        import com.google.firebase.database.DataSnapshot
        import com.google.firebase.database.DatabaseError
        import com.google.firebase.database.FirebaseDatabase
        import com.google.firebase.database.ValueEventListener

        class   LoginScreen : AppCompatActivity() {
            private lateinit var usernameEditText:EditText
            private lateinit var passwordEditText:EditText
            private lateinit var passwordToggle:ImageView

            override fun onCreate(savedInstanceState:Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_login_screen)



                val clickHereTextView = findViewById<TextView>(R.id.createAccountTextView)
                usernameEditText = findViewById(R.id.usernameEditText)
                passwordEditText = findViewById(R.id.passwordEditText)
                val loginButton:Button = findViewById(R.id.loginButton)





                loginButton.setOnClickListener {
                    val username = usernameEditText.text.toString()
                    val password = passwordEditText.text.toString()

                    if (username.isNotEmpty() && password.isNotEmpty()) {
                        isValidLogin(username, password)
                    } else {
                        showToast("Please enter username and password.")
                    }
                }

                clickHereTextView.setOnClickListener {
                    val intent = Intent(this, RegisterScreen::class.java)
                    startActivity(intent)
                    finish()
                }
            }


            private fun isValidLogin(username: String, password: String) {
                val auth = FirebaseAuth.getInstance()
                if (auth.currentUser != null) {
                    // User is already authenticated, so go to UserActivity

                }

                auth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser

                            if (user != null) {
                                val userId = user.uid
                                val databaseReference =
                                    FirebaseDatabase.getInstance().getReference("users/$userId")

                                databaseReference.addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(dataSnapshot:DataSnapshot) {
                                        if (dataSnapshot.exists()) {
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
                                            showToast("User data not found")
                                        }
                                    }

                                    override fun onCancelled(databaseError:DatabaseError) {
                                        Log.e(
                                            "FirebaseDatabase",
                                            "Error fetching user data: ${databaseError.message}"
                                        )
                                        showToast("Error: ${databaseError.message}")
                                    }
                                })
                            }
                        } else {
                            showToast("Login Failed. Please check your credentials.")
                        }
                    }
            }


            private fun showToast(message: String) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
