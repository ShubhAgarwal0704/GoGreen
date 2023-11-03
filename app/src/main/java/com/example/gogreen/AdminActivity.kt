    package com.example.gogreen

    import android.content.Intent
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.widget.Button
    import android.widget.Toast
    import androidx.fragment.app.Fragment
    import com.example.gogreen.Login_Register.LoginScreen
    import com.google.android.material.bottomnavigation.BottomNavigationView
    import com.google.firebase.auth.FirebaseAuth

    class AdminActivity : AppCompatActivity() {
        private lateinit var auth : FirebaseAuth
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_admin_activity)
            auth =  FirebaseAuth.getInstance()
            val logoutbutton = findViewById<Button>(R.id.logoutbutton)
            logoutbutton.setOnClickListener {
                auth.signOut()
                Toast.makeText(this@AdminActivity, "User Logged out!!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginScreen::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
            // Set the HomeFragment as the initial fragment
