package com.example.febafriends

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.febafriends.databinding.ActivitySongsListBinding

class SongsList : AppCompatActivity() {

    companion object {
        lateinit var category : CategoryModel
    }
    lateinit var binding : ActivitySongsListBinding
    lateinit var songsAdapter: SongsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarSongs)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbarSongs.navigationIcon?.setTint(resources.getColor(android.R.color.holo_red_dark))


        binding.Songs.text = category.name
        setRecycleview()

    }

    fun setRecycleview(){
        songsAdapter = SongsAdapter(category.songs)
        binding.songsrecycleView.layoutManager = LinearLayoutManager(this)
        binding.songsrecycleView.adapter = songsAdapter

    }
}