package com.example.gogreen.user_Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.codebyashish.autoimageslider.Enums.ImageScaleType
import com.codebyashish.autoimageslider.Models.ImageSlidesModel
import com.example.gogreen.UsersActivity

import com.example.gogreen.databinding.ActivityProductDetailBinding
import com.example.gogreen.roomdb.AppDatabase
import com.example.gogreen.roomdb.ProductDao
import com.example.gogreen.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProductDetailBinding
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        getProductDetails(intent.getStringExtra("id"))
        setContentView(binding.root)


    }

    @SuppressLint("SetTextI18n")
    private fun getProductDetails(proId:String?) {

        Firebase.firestore.collection("Products")
            .document(proId!!).get().addOnSuccessListener {
                val list = it.get("productImages") as ArrayList<String>
                val name = it.getString("productName")
                val productSp = it.getString("productPrice")
                val productDesc = it.getString("productDescription")

                binding.textView7.text = name
                binding.textView8.text = "₹$productSp"
                binding.textView9.text = productDesc


                val slidList : ArrayList<ImageSlidesModel> = ArrayList()
                for (data in list) {
                    slidList.add(ImageSlidesModel(data, ImageScaleType.CENTER_CROP))
                }
                cartAction(proId, name, productSp, it.getString("productCoverImg"))
                binding.imageSlider.setImageList(slidList)


            }.addOnFailureListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }
    private fun cartAction(proId: String, name: String?, productSp: String?, coverimg: String?) {

        val productDao = AppDatabase.getInstance(this).productDao()

        if (productDao.isExit(proId) != null) {
            binding.textView10.text = "Go to Cart"
        } else {
            binding.textView10.text = "Add to Cart"
        }

        binding.textView10.setOnClickListener {
            if (productDao.isExit(proId) != null) {
                openCart()
            } else {
                addtoCart(productDao, proId, name, coverimg, productSp)
            }
        }
    }

    private fun addtoCart(
        productDao:ProductDao,
        proId: String,
        name: String?,
        coverimg: String?,
        productSp: String?,
    ) {
        val data = ProductModel(proId, name, coverimg, productSp)

        lifecycleScope.launch(Dispatchers.IO) {
            productDao.insertProduct(data)
            binding.textView10.text = "Go to Cart"
        }
    }

    private fun openCart() {
        val preferences = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("isCart", true)
        editor.apply()

        startActivity(Intent(this, UsersActivity::class.java))
        finish()
    }

}

