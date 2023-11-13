package com.example.gogreen.userFragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.codebyashish.autoimageslider.Enums.ImageAnimationTypes
import com.codebyashish.autoimageslider.Enums.ImageScaleType
import com.codebyashish.autoimageslider.Models.ImageSlidesModel
import com.example.gogreen.R

import com.example.gogreen.admin_Model.category_Model
import com.example.gogreen.databinding.FragmentHomeBinding
import com.example.gogreen.userAdapter.CategoryAdapter
import com.example.gogreen.userAdapter.ProductuserAdapter
import com.example.gogreen.user_Model.addProductuserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var categoryAdapter : CategoryAdapter? = null
    private var productuserAdapter : ProductuserAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        getCategories()
        getProducts()

        val autoImageSlider = binding.slider
        val autoImageList: ArrayList<ImageSlidesModel> = ArrayList()

        autoImageList.add(
            ImageSlidesModel(
                "https://source.unsplash.com/300x200/?eco_friendly",
                "Go Green! Go Eco-Friendly!"
            )
        )
        autoImageList.add(
            ImageSlidesModel(
                "https://source.unsplash.com/300x200/?jute_bags",
                "Join the Green Side!"
            )
        )
        autoImageList.add(
            ImageSlidesModel(
                "https://source.unsplash.com/300x200/?paper_bags",
                "Let's make the Earth a better place!"
            )
        )

        autoImageSlider.setImageList(autoImageList, ImageScaleType.CENTER_CROP)

        autoImageSlider.setSlideAnimation(ImageAnimationTypes.DEPTH_SLIDE)

        return binding.root
    }

    private fun getProducts() {
        val list = ArrayList<addProductuserModel>()
        Firebase.firestore.collection("Products")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(addProductuserModel::class.java)
                    list.add(data!!)
                }
                productuserAdapter = ProductuserAdapter(requireContext(), ArrayList(list), list)
                binding.productRecyclerview.adapter = productuserAdapter
                binding.searchEditText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        productuserAdapter?.filter?.filter(s.toString())
                    }
                })
            }
    }

    private fun getCategories() {
        val list = ArrayList<category_Model>()
        Firebase.firestore.collection("cate")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(category_Model::class.java)
                    list.add(data!!)
                }
                categoryAdapter = CategoryAdapter(requireContext(), list)
                binding.categoryRecyclerview.adapter = categoryAdapter
            }
    }

}


