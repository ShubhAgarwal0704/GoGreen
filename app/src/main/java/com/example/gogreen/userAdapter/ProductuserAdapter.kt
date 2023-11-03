package com.example.gogreen.userAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.gogreen.databinding.LayoutProductItemBinding
import com.example.gogreen.user_Activity.ProductDetailActivity
import com.example.gogreen.user_Model.addProductuserModel


class ProductuserAdapter(val context:Context, val list: ArrayList<addProductuserModel>) :
    RecyclerView.Adapter<ProductuserAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding:LayoutProductItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): ProductViewHolder {
        val binding = LayoutProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val data = list[position]

        Glide.with(context).load(data.productCoverImg).into(holder.binding.imageView2)

        holder.binding.textView2.text = data.productName
        holder.binding.textView3.text = data.productCategory
        holder.binding.button.text = "₹"+data.productPrice
       // holder.binding.button2.text = data.p

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("id", list[position].productId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}