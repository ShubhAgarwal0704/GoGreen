package com.example.gogreen.userFragments

import android.content.Intent
import android.graphics.Bitmap
import android.media.audiofx.DynamicsProcessing
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.gogreen.Login_Register.LoginScreen
import com.example.gogreen.R
import com.example.gogreen.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentProfileBinding.inflate(layoutInflater)
        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .get().addOnSuccessListener {

                    binding.name.text = it.child("username").value.toString()
                    binding.useremail.text = it.child("email").value.toString()
                    binding.address.text = it.child("address").value.toString()
                    binding.userName.text = it.child("name").value.toString()
                    binding.gender.text = it.child("gender").value.toString()
                    binding.password.text = it.child("password").value.toString()

            }.addOnFailureListener { e ->
                // Handle the error here
                Toast.makeText(requireContext(), "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        return binding.root
    }
}