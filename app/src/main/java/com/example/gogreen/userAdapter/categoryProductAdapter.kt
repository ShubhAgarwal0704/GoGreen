package com.example.gogreen.userAdapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.gogreen.databinding.ItemCategoryProductUserBinding
import com.example.gogreen.user_Activity.ProductDetailActivity
import com.example.gogreen.user_Model.addProductuserModel

class categoryProductAdapter(val context: Context, val list: ArrayList<addProductuserModel>) :
    RecyclerView.Adapter<categoryProductAdapter.CategoryProductViewHolder>()
{

    inner class CategoryProductViewHolder(val binding:ItemCategoryProductUserBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): CategoryProductViewHolder {
        val binding = ItemCategoryProductUserBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return CategoryProductViewHolder(binding)

    }

    override fun onBindViewHolder(holder: CategoryProductViewHolder, position: Int) {

        Glide.with(context).load(list[position].productCoverImg).into(holder.binding.imageView3)

        holder.binding.textView5.text = list[position].productName
        holder.binding.textView6.text = list[position].productPrice

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