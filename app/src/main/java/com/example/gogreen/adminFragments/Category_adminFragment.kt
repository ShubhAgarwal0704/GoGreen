package com.example.gogreen.adminFragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.gogreen.R
import com.example.gogreen.admin_Adapter.CategoryAdapter
import com.example.gogreen.admin_Model.category_Model
import com.example.gogreen.databinding.FragmentCategoryAdminBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import java.util.Locale.Category
import kotlin.collections.ArrayList


class Category_adminFragment : Fragment() {
    private lateinit var binding:FragmentCategoryAdminBinding
    private var imageUrl:Uri? = null
    private lateinit var dialog:Dialog


    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            imageUrl = it.data!!.data
            binding.imageView.setImageURI(imageUrl)
        }
    }

    override fun onCreateView(
        inflater:LayoutInflater, container:ViewGroup?,
        savedInstanceState:Bundle?,
    ):View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryAdminBinding.inflate(layoutInflater)
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.prgress_layout)
        dialog.setCancelable(false)


       getData()

        binding.apply {
            imageView.setOnClickListener {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                launchGalleryActivity.launch(intent)
            }
            button.setOnClickListener {

                validateData(binding.categry.text.toString())

            }
        }

        return binding.root
    }


    private fun getData() {
        val list = ArrayList<category_Model>()
        Firebase.firestore.collection("cate")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents){
                    val data = doc.toObject(category_Model::class.java)
                    list.add(data!!)
                }
                binding.categryRecycler.adapter= CategoryAdapter(requireContext(),list)
            }
    }

    private fun validateData(categry:String) {
        if (categry.isEmpty()) {
            Toast.makeText(requireContext(), "please provide category name", Toast.LENGTH_SHORT)
                .show()
        } else if (imageUrl == null) {
            Toast.makeText(requireContext(), "please select image", Toast.LENGTH_SHORT).show()

        } else {
            uploadImage(categry)
        }

    }

    private fun uploadImage(categry:String) {
        dialog.show()

        val fileName = UUID.randomUUID().toString() + ".jpg"

        val refStorage = FirebaseStorage.getInstance().reference.child("categry/$fileName")
        refStorage.putFile(imageUrl!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    storeData(categry, image.toString())

                }
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show()


            }

    }

    private fun storeData(categry:String, Url:String) {
        val db = Firebase.firestore
        val data = com.example.gogreen.Category(categry, Url)
        db.collection("cate").add(data)
            .addOnSuccessListener {
                dialog.dismiss()
                binding.imageView.setImageDrawable(resources.getDrawable(R.drawable.vector))
                binding.categry.text = null
               getData()
                Toast.makeText(requireContext(), "category updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                dialog.dismiss()
                Log.e("FirebaseError", "Error adding category", e)
                Toast.makeText(
                    requireContext(),
                    "Something went wrong: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

        }


    }

    }







