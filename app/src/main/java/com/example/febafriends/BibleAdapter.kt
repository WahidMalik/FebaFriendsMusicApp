package com.example.febafriends

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.febafriends.databinding.ItemsubcategoryBinding
import com.google.firebase.firestore.FirebaseFirestore

class BibleAdapter(private val list: List<String>) : RecyclerView.Adapter<BibleAdapter.MyViewHolder>() {

    inner class MyViewHolder(private val binding: ItemsubcategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(databible: String) {
            FirebaseFirestore.getInstance().collection("Bible")
                .document(databible).get().addOnSuccessListener { document ->
                    val bibleData = document.toObject(Bibledata::class.java)
                    bibleData?.let {
                        binding.bibleName.text = it.title
                        binding.layoutName.setOnClickListener {
                            Exoplayer.startPlaying(binding.root.context, bibleData)
                        }
                    }
                }.addOnFailureListener {
                    // Handle failure
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
