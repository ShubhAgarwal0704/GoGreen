package com.example.gogreen.user_Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.gogreen.R
import com.example.gogreen.userAdapter.ProductuserAdapter
import com.example.gogreen.user_Model.addProductuserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Category_user_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_user)


        getProductes(intent.getStringExtra("cate"))

    }

    private fun getProductes(category: String?) {
        val list = ArrayList<addProductuserModel>()
        Firebase.firestore.collection("Products").whereEqualTo("productCategory",category)
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(addProductuserModel::class.java)
                    list.add(data!!)
                }
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
              recyclerView.adapter = ProductuserAdapter(this,list)
            }
    }
}