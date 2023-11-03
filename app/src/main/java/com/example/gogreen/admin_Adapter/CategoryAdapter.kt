package com.example.gogreen.admin_Adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gogreen.admin_Model.category_Model
import com.example.gogreen.databinding.ItemCategoryAdminBinding


class CategoryAdapter (var context:Context, val list:ArrayList<category_Model>) :
RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(var binding:ItemCategoryAdminBinding) :
        RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryAdminBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
           holder.binding.textView.text =list[position].cate
          Glide.with(context).load(list[position].image).into(holder.binding.imageView2)

    }

    override fun getItemCount(): Int {
        return list.size
    }
}