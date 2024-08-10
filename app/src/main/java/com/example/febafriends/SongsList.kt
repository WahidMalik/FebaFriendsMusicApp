package com.example.febafriends

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.febafriends.databinding.ActivitySongsListBinding
import com.google.firebase.firestore.FirebaseFirestore

class SongsList : AppCompatActivity() {

    companion object {
        lateinit var category: CategoryModel
    }

    private lateinit var binding: ActivitySongsListBinding
    private lateinit var songsAdapter: SongsAdapter
    private lateinit var originalSongsList: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        setSupportActionBar(binding.toolbarSongs)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbarSongs.navigationIcon?.setTint(resources.getColor(android.R.color.holo_red_dark))

        binding.Songs.text = category.name
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        originalSongsList = category.songs
        songsAdapter = SongsAdapter(originalSongsList)
        binding.songsrecycleView.layoutManager = LinearLayoutManager(this)
        binding.songsrecycleView.adapter = songsAdapter
    }


}
