package com.example.gogreen.User_Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.gogreen.R
import com.example.gogreen.databinding.ActivityAddressBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddressBinding
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_address)

      /*  binding.proceedtocheckout.setOnClickListener {
            val intent = Intent(this, checkoutActivity::class.java)


            intent.putExtras()
            startActivity(intent)
        }*/
    }
}