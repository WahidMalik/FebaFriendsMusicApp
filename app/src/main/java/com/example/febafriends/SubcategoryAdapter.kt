package com.example.febafriends

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.febafriends.databinding.ItemsubcategoryBinding

class SubcategoryAdapter(private val list: List<Subcategory>) : RecyclerView.Adapter<SubcategoryAdapter.MyViewHolder>() {



    inner class MyViewHolder(val binding: ItemsubcategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Subcategory) {
            binding.bibleName.text = data.name
            val context = binding.root.context
            binding.layoutName.setOnClickListener {
                ChapterListActivity.bibleList = data
                context.startActivity(Intent(context, ChapterListActivity::class.java))
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemsubcategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }


}
