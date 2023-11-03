package com.example.gogreen.userAdapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gogreen.user_Activity.Category_user_Activity
import com.example.gogreen.admin_Model.category_Model
import com.example.gogreen.databinding.LayoutCategoryUserItemBinding


class CategoryAdapter (var context:Context, val list:ArrayList<category_Model>) :
RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(var binding:LayoutCategoryUserItemBinding) :
        RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = LayoutCategoryUserItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
           holder.binding.textView2.text =list[position].cate
          Glide.with(context).load(list[position].image).into(holder.binding.imageView)

        holder.itemView.setOnClickListener {
           val intent = Intent(context,Category_user_Activity::class.java)
            intent.putExtra("cate",list[position].cate)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}