package com.example.febafriends

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
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
        setupSearchView()
    }

    private fun setupRecyclerView() {
        originalSongsList = category.songs
        songsAdapter = SongsAdapter(originalSongsList)
        binding.songsrecycleView.layoutManager = LinearLayoutManager(this)
        binding.songsrecycleView.adapter = songsAdapter
    }

    private fun setupSearchView() {
        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed here
            }

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                val filteredSongs = if (query.isNotEmpty()) {
                    originalSongsList.filter { song ->
                        // Perform a case-insensitive match or use other criteria as needed
                        song.lowercase().contains(query.lowercase())
                    }
                } else {
                    originalSongsList
                }

                if (filteredSongs.isEmpty()) {
                    binding.songsrecycleView.visibility = View.GONE
                } else {
                    binding.songsrecycleView.visibility = View.VISIBLE
                }

                songsAdapter.updateSongs(filteredSongs)
            }
        })
    }
}
