package com.example.gogreen.adminFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.gogreen.R
import com.example.gogreen.databinding.FragmentHomeAdminBinding
import com.example.gogreen.databinding.FragmentHomeBinding


class Home_adminFragment : Fragment() {

    private lateinit var binding:FragmentHomeAdminBinding



    override fun onCreateView(
        inflater:LayoutInflater, container:ViewGroup?,
        savedInstanceState:Bundle?,
    ):View? {
        binding=FragmentHomeAdminBinding.inflate(layoutInflater)

        binding.addcatButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_adminFragment_to_category_adminFragment)
        }
        binding.addprodButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_adminFragment_to_addProduct_adminFragment)
        }
       binding.addsliderButton.setOnClickListener{
            findNavController().navigate(R.id.action_home_adminFragment_to_slider_admin_Fragment)
        }


        return binding.root
    }


}