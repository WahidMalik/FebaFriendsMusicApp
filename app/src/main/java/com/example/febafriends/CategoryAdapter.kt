package com.example.febafriends

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.febafriends.databinding.CategoryLayoutBinding

class CategoryAdapter(private val categoryList : List<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding : CategoryLayoutBinding) : RecyclerView.ViewHolder(binding.root){
            fun bindData(category : CategoryModel){
                binding.categoryName.text = category.name
              val context = binding.root.context
                binding.layoutName.setOnClickListener {
                    SongsList.category = category
                    context.startActivity(Intent(context,SongsList::class.java))
                }

            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CategoryLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(categoryList[position])
    }
}