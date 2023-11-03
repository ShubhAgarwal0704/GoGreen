package com.example.gogreen.adminFragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.gogreen.R
import com.example.gogreen.admin_Adapter.addProductImageAdapter
import com.example.gogreen.admin_Model.addProductModel
import com.example.gogreen.admin_Model.category_Model
import com.example.gogreen.databinding.FragmentAddProductAdminBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class addProduct_adminFragment : Fragment() {
     private lateinit var binding:FragmentAddProductAdminBinding
     private lateinit var list:ArrayList<Uri>
    private lateinit var listImages:ArrayList<String>
    private lateinit var adapter :addProductImageAdapter
    private var coverImage : Uri?= null
    private lateinit var dialog:Dialog
    private lateinit var categoryList : ArrayList<String>
    private var coverImgUrl :String?=null

    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            coverImage = it.data!!.data
            binding.productCoverImg.setImageURI(coverImage)
            binding.productCoverImg.visibility =  VISIBLE
        }
    }
    private var launchProductActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val imageUrl = it.data!!.data
            list.add(imageUrl!!)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(
        inflater:LayoutInflater, container:ViewGroup?,
        savedInstanceState:Bundle?,
    ):View? {
        binding = FragmentAddProductAdminBinding.inflate(layoutInflater)


        list = ArrayList()
        listImages = ArrayList()
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.prgress_layout)
        dialog.setCancelable(false)

       binding.selectcoverimg.setOnClickListener {
              val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                launchGalleryActivity.launch(intent)
        }
        binding.prodImgButton.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchProductActivity.launch(intent)
        }
        adapter =addProductImageAdapter(list)
        binding.productImgRecyclerView.adapter = adapter

        binding.submitButton.setOnClickListener{
            validateData()
        }

        setProductrCategory()
        return binding.root
    }

    private fun validateData() {
       if (binding.Productname.text.toString().isEmpty()){
           binding.Productname.requestFocus()
           binding.Productname.error= "Empty"
       }else if (binding.Productprice.text.toString().isEmpty()){
           binding.Productprice.error = "Empty"
       }else if (coverImage== null){
           Toast.makeText(requireContext(),"Please select cover image",Toast.LENGTH_SHORT).show()
       }else if (list.size<1){
           Toast.makeText(requireContext(),"please select product images",Toast.LENGTH_SHORT).show()
       }else{
           uploadImage()
       }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun uploadImage() {
        dialog.show()

        val fileName = UUID.randomUUID().toString() + ".jpg"

        val refStorage = FirebaseStorage.getInstance().reference.child("Products/$fileName")
        refStorage.putFile(coverImage!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                 coverImgUrl = image.toString()
                    uploadProductImage()
                }
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show()


            }
    }
     private var i =0
    private fun uploadProductImage() {
        dialog.show()

        val fileName = UUID.randomUUID().toString() + ".jpg"

        val refStorage = FirebaseStorage.getInstance().reference.child("Products/$fileName")
        refStorage.putFile(list[i])
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    listImages.add(image!!.toString())
                    if(list.size == listImages.size){
                        storeData()
                    }else{
                        i++
                        uploadProductImage()
                    }
                }
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show()


            }

    }

    private fun storeData() {
        val db = Firebase.firestore.collection("Products")
                val key = db.document().id

                  val data = addProductModel(
                 binding.Productname.text.toString(),
                 binding.Productdesc.text.toString(),
                   coverImgUrl.toString(),
                   categoryList[binding.addProductDropdown.selectedItemPosition],
                     key,
                   binding.Productprice.text.toString(),
                   listImages
               )
                 db.document(key).set(data).addOnSuccessListener {
                dialog.dismiss()

                Toast.makeText(requireContext(), "Product added", Toast.LENGTH_SHORT).show()
                     binding.Productname.text = null
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



    private fun setProductrCategory(){
           categoryList = ArrayList()
           Firebase.firestore.collection("cate")
               .get().addOnSuccessListener {
                 categoryList.clear()
                   for (doc in it.documents){
                       val data = doc.toObject(category_Model::class.java)
                       categoryList.add(data!!.cate!!)
                   }
                   categoryList.add(0,"Select category")

                   val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,categoryList)
                   binding.addProductDropdown.adapter = arrayAdapter

               }
       }

    }
