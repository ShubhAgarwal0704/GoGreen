package com.example.gogreen.admin_Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gogreen.databinding.ImageItemBinding

class addProductImageAdapter ( val list: ArrayList<Uri>) :
    RecyclerView.Adapter<addProductImageAdapter.addProductViewHolder>() {

    inner class addProductViewHolder(val binding:ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int):addProductViewHolder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return addProductViewHolder(binding)

    }

    override fun onBindViewHolder(holder:addProductViewHolder, position:Int) {
        holder.binding.imageitem.setImageURI(list[position])

    }
    override fun getItemCount():Int {
       return  list.size
    }

}