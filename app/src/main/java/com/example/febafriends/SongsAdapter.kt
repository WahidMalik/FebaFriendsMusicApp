package com.example.febafriends

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.febafriends.databinding.SongsLayoutBinding
import com.google.firebase.firestore.FirebaseFirestore

class SongsAdapter(private val songList : List<String>) : RecyclerView.Adapter<SongsAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding : SongsLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindData(song: String){
            FirebaseFirestore.getInstance().collection("songs")
                .document(song).get().addOnSuccessListener {
                    val song = it.toObject(SongsData::class.java)
                    song?.apply {
                        binding.songName.text = title
                        binding.layoutNamesong.setOnClickListener {
                            Exoplayer.startPlaying(binding.root.context,song)
                            it.context.startActivity(Intent(it.context,SongPlay::class.java))
                        }
                    }
                }.addOnFailureListener {

                }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = SongsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(songList[position])
    }
}